package mks.myworkspace.cvhub.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mks.myworkspace.cvhub.entity.User;
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
import mks.myworkspace.cvhub.service.UserService;

@Controller
public class SignController extends BaseController {
    
    @Autowired
    private UserService userService;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
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

    // Logout
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/home";
    }

    // Show register form
    @GetMapping("/register")
    public ModelAndView showRegisterForm() {
        ModelAndView mav = new ModelAndView("/signInOut/signup");
        return mav;
    }

    // Process registration
    @PostMapping("/register")
    public ModelAndView processRegistration(@ModelAttribute UserDTO userDTO, 
            BindingResult result, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();

        try {
            // Validate email existence
            if (userService.isEmailExists(userDTO.getEmail())) {
                mav.addObject("error", "Email đã tồn tại trong hệ thống");
                mav.setViewName("/signInOut/signup");
                return mav;
            }

            // Validate password match
            if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
                mav.addObject("error", "Mật khẩu xác nhận không khớp");
                mav.setViewName("/signInOut/signup");
                return mav;
            }

            // Register user
            User user = userService.createUser(userDTO.getFullName(), userDTO.getEmail(), 
            		passwordEncoder.encode(userDTO.getPassword()), userDTO.getPhone());

            // Redirect to login with success message
            mav.setViewName("redirect:/login");
            redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");

        } catch (Exception e) {
            mav.setViewName("/signInOut/signup");
            mav.addObject("error", e.getMessage());
        }

        return mav;
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