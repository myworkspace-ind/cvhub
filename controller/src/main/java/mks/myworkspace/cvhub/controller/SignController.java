package mks.myworkspace.cvhub.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mks.myworkspace.cvhub.entity.EmailVerification;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.repository.EmailVerificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mks.myworkspace.cvhub.controller.model.UserDTO;
import mks.myworkspace.cvhub.service.EmailService;
import mks.myworkspace.cvhub.service.UserService;
import mks.myworkspace.cvhub.service.impl.Pbkdf2PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Controller
public class SignController extends BaseController {
	@Value("${user.register:0}")
    private int userRegister; 
    @Autowired
    private UserService userService;
    @Value("${register.user.confirm:false}")
    private boolean registerUserConfirm;

    @Autowired
    private EmailService emailService;
    @Autowired
    private EmailVerificationRepository emailVerificationRepository;
   
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    Pbkdf2PasswordEncoder passwordEncoder1 = new Pbkdf2PasswordEncoder();
   
   /*
    // Login page
    @GetMapping("/login")
    public ModelAndView showLoginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {
        
        ModelAndView model = new ModelAndView();
        
        if (error != null) {
            model.addObject("error", "Email hoặc mật khẩu không chính xác!");
        }

        if (logout != null) {
            model.addObject("message", "Bạn đã đăng xuất thành công!");
        }

        model.setViewName("/signInOut/signin");
        return model;
    }

*/
    
    @GetMapping("/login")
    public String showLoginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (error != null) {
            redirectAttributes.addFlashAttribute("error", "Email hoặc mật khẩu không chính xác!");
        }

        if (logout != null) {
            redirectAttributes.addFlashAttribute("message", "Bạn đã đăng xuất thành công!");
        }

        return "redirect:/login-view";
    }

    @GetMapping("/login-view")
    public String showLoginView(Model model) {
        return "/signInOut/signin";
    }

    
    // Logout
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            redirectAttributes.addFlashAttribute("message", "Bạn đã đăng xuất thành công!");
        }
        return "redirect:/login";
    }


    // Show register form
    @GetMapping("/register")
    public ModelAndView showRegisterForm() {
        ModelAndView mav = new ModelAndView("/signInOut/signup");
        return mav;
    }
    private String generateVerificationCode() {
        int length = 6;  // Length of the verification code
        StringBuilder code = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            code.append(characters.charAt(index));
        }
        return code.toString();
    }
    // Process registration
    @PostMapping("/register")
    public ModelAndView processRegistration(@ModelAttribute UserDTO userDTO, 
            BindingResult result, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();

        try {
            if (userRegister == 1) {
                // Nếu `user.register = 1`, sử dụng phương thức đăng ký vào Sakai
                String encodedPassword = passwordEncoder1.encode(userDTO.getPassword());
                String prefixedPassword = "PBKDF2:" + encodedPassword;

                userService.registerUserInSakai(userDTO.getFullName(), userDTO.getEmail(), prefixedPassword, userDTO.getPhone());
                redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
                mav.setViewName("redirect:/login");
            } else {
                // Nếu không có key `user.register = 1`, sử dụng phương thức đăng ký mặc định
                if (userService.isEmailExists(userDTO.getEmail())) {
                    mav.addObject("error", "Email đã tồn tại trong hệ thống");
                    mav.setViewName("/signInOut/signup");
                    return mav;
                }

                if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
                    mav.addObject("error", "Mật khẩu xác nhận không khớp");
                    mav.setViewName("/signInOut/signup");
                    return mav;
                }
                
                String combinedPhone = "(" + userDTO.getDialCode() + ") " + userDTO.getPhone();
                // Tạo người dùng mới
                User user = userService.createUser(userDTO.getFullName(), userDTO.getEmail(), 
                        passwordEncoder.encode(userDTO.getPassword()), combinedPhone);
                
                // Gửi email xác nhận
                if (registerUserConfirm) {
                    // Nếu cần gửi email xác nhận
                    String verificationCode = generateVerificationCode();
                    emailService.sendEmail(user.getEmail(), "Xác nhận email", 
                            "Vui lòng xác nhận tài khoản của bạn bằng mã sau: " + verificationCode);

                    // Lưu mã xác nhận vào cơ sở dữ liệu
                    emailVerificationRepository.save(new EmailVerification(user.getEmail(), verificationCode));

                    redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng kiểm tra email của bạn để xác nhận tài khoản.");
                    mav.setViewName("redirect:/verify");
                } else {
                    // Không cần xác nhận qua email
                    redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
                    mav.setViewName("redirect:/login");
                }
            }
        } catch (Exception e) {
            mav.addObject("error", e.getMessage());
            mav.setViewName("/signInOut/signup");
        }

        return mav;
    }
    @PostMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String email, @RequestParam String code) {
        try {
            boolean success = emailService.verifyEmail(email, code);
            if (success) {
                return ResponseEntity.ok("Email verified successfully, status updated to 'accept'");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification code");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/verify")
    public String showVerificationPage(Model model) {
        model.addAttribute("message", "Vui lòng nhập mã xác nhận đã gửi đến email của bạn.");
        return "/signInOut/verify";  // Trả về tên view (ví dụ: verify.html)
    }


    // Helper method to get current logged in user
    protected User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return userService.findUserByEmail(auth.getName());
        }
        return null;
    }
}