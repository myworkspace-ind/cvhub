package mks.myworkspace.cvhub.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mks.myworkspace.cvhub.entity.CV;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.controller.model.UserDTO;
import mks.myworkspace.cvhub.service.CvService;
import mks.myworkspace.cvhub.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private CvService cvService;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/profile/edit")
    public ModelAndView showPersonalSettings(Model model) {
    	ModelAndView mav = new ModelAndView();
        // Get current user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByEmail(auth.getName());
        // Create settings object
        UserDTO settings = new UserDTO();
        settings.setFullName(currentUser.getFullName());
        settings.setEmail(currentUser.getEmail());
        settings.setPhone(currentUser.getPhone());
        
        // Add data to model
        mav.addObject("user", currentUser);
        mav.addObject("userSettings", settings);
        mav.addObject("selectedCvCount", cvService.getSelectedCVCount(currentUser.getId()));
        mav.setViewName("/signInOut/editUser");
        return mav;
    }
    @GetMapping("/change-password")
    public ModelAndView showChangePasswordPagee() {
        ModelAndView mav = new ModelAndView("/signInOut/changePassword");
        return mav;
    }
    @PostMapping("/profile/changepassword")
    public ModelAndView changePassword(@RequestParam("oldPassword") String oldPassword,
                                     @RequestParam("newPassword") String newPassword,
                                     @RequestParam("confirmPassword") String confirmPassword) {
        ModelAndView mav = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByEmail(auth.getName());

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            mav.addObject("error", "Mật khẩu cũ không chính xác");
            mav.setViewName("/signInOut/editUser");
            mav.addObject("user", currentUser);
            mav.addObject("userSettings", currentUser);
            return mav;
        }
        if (passwordEncoder.matches(newPassword, currentUser.getPassword())) {
            mav.addObject("error", "Mật khẩu mới không được giống mật khẩu cũ");
            mav.setViewName("/signInOut/editUser");
            mav.addObject("user", currentUser);
            mav.addObject("userSettings", currentUser);
            return mav;
        }

        // Kiểm tra mật khẩu mới và xác nhận
        if (!newPassword.equals(confirmPassword)) {
            mav.addObject("error", "Mật khẩu mới và xác nhận mật khẩu không khớp");
            mav.setViewName("/signInOut/editUser");
            mav.addObject("user", currentUser);
            mav.addObject("userSettings", currentUser);
            return mav;
        }

        try {
            currentUser.setPassword(passwordEncoder.encode(newPassword));
            userService.getRepo().save(currentUser);
            mav.addObject("success", "Thay đổi mật khẩu thành công");
        } catch (Exception e) {
            mav.addObject("error", "Có lỗi xảy ra khi thay đổi mật khẩu");
        }
        mav.addObject("user", currentUser);
        mav.addObject("userSettings", currentUser);

        mav.setViewName("/signInOut/editUser");
        return mav;
    }
    @PostMapping("/profile/edit")
    public ModelAndView updatePersonalSettings(@ModelAttribute UserDTO settings, 
                                       RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByEmail(auth.getName());
        
        try {
            // Update only fields that are not null or empty
            if (settings.getFullName() != null && !settings.getFullName().trim().isEmpty()) {
                currentUser.setFullName(settings.getFullName());
            }
            
            if (settings.getPhone() != null && !settings.getPhone().trim().isEmpty()) {
                // Kiểm tra độ dài số điện thoại
                if (settings.getPhone().length() > 15) {
                    throw new IllegalArgumentException("Số điện thoại không được vượt quá 15 ký tự");
                }
                // Kiểm tra số điện thoại chỉ chứa số
                if (!settings.getPhone().matches("\\d+")) {
                    throw new IllegalArgumentException("Số điện thoại chỉ được chứa số");
                }
                currentUser.setPhone(settings.getPhone());
            }
            
            // Save updates
            userService.getRepo().save(currentUser);
            
            // Refresh user data for display
            UserDTO updatedSettings = new UserDTO();
            updatedSettings.setFullName(currentUser.getFullName());
            updatedSettings.setEmail(currentUser.getEmail());
            updatedSettings.setPhone(currentUser.getPhone());
            
            mav.addObject("success", "Cập nhật thông tin thành công");
            mav.addObject("userSettings", updatedSettings);
            
        } catch (IllegalArgumentException e) {
            // Xử lý lỗi validation
            mav.addObject("error", e.getMessage());
            mav.addObject("userSettings", settings);
        } catch (Exception e) {
            // Xử lý các lỗi khác
            mav.addObject("error", "Có lỗi xảy ra khi cập nhật thông tin");
            mav.addObject("userSettings", settings);
        }
        
        // Luôn thêm các object cần thiết vào model
        mav.addObject("user", currentUser);
        mav.addObject("selectedCvCount", cvService.getSelectedCVCount(currentUser.getId()));
        mav.setViewName("/signInOut/editUser");
        return mav;
    }
    
 // API để upload ảnh
 	@PostMapping("/profile/upload_avatar")
 	public String uploadAvatar(@RequestParam("file") MultipartFile file) {
 		try {
 			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
 			User currentUser = userService.findUserByEmail(auth.getName());
 			currentUser.setImage(file.getBytes()); // Lưu ảnh dưới dạng byte[]
 			// Save updates
 			userService.getRepo().save(currentUser);
 			return "redirect:/profile/edit";
 		} catch (Exception e) {
 			return "redirect:/profile/edit";
 		}
 	}

 	@GetMapping(value = "/profile/avatar")
 	@ResponseBody
 	public ResponseEntity<byte[]> getImage() throws IOException {
 	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
 	    User currentUser = userService.findUserByEmail(auth.getName());
 	    byte[] image = currentUser.getImage();

 	    if (image == null || image.length == 0) {
 	    // Đường dẫn tuyệt đối tới resource trong module cvhub-web
 	    	
 	    	 // Tạo ảnh mặc định từ chữ cái đầu email
 	        String email = currentUser.getEmail();
 	        image = createDefaultAvatar(email);
 	    }

 	    return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
 	}
 	
 	
 	private byte[] createDefaultAvatar(String email) throws IOException {
 	    // Tính toán hash của email (chuyển email thành chuỗi băm)
 	    int hash = email.hashCode(); // Tính hash của email

 	    // Chuyển đổi hash thành chuỗi màu thập lục phân (#RRGGBB)
 	    String hexColor = String.format("#%06X", (0xFFFFFF & hash));  // Lấy 24 bit cuối của hash và chuyển thành mã màu hex

 	    // Chuyển mã màu hex thành đối tượng Color
 	    Color backgroundColor = Color.decode(hexColor); // Chuyển mã hex thành đối tượng Color

 	    // Tạo ảnh rỗng
 	    int width = 200; // Chiều rộng ảnh
 	    int height = 200; // Chiều cao ảnh
 	    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
 	    Graphics2D g2d = bufferedImage.createGraphics();

 	    // Vẽ nền
 	    g2d.setColor(backgroundColor); // Màu nền
 	    g2d.fillRect(0, 0, width, height);

 	    // Lấy chữ cái đầu (in hoa) để vẽ lên avatar
 	    char initial = email.toUpperCase().charAt(0); // Lấy chữ cái đầu (in hoa)
 	    g2d.setColor(Color.WHITE); // Màu chữ
 	    g2d.setFont(new Font("Arial", Font.BOLD, 100)); // Font chữ
 	    FontMetrics fm = g2d.getFontMetrics();
 	    int textWidth = fm.stringWidth(String.valueOf(initial));
 	    int textHeight = fm.getAscent();

 	    // Căn giữa chữ
 	    int x = (width - textWidth) / 2;
 	    int y = (height - 10 + textHeight) / 2;

 	    g2d.drawString(String.valueOf(initial), x, y);

 	    g2d.dispose(); // Đóng Graphics2D

 	    // Chuyển BufferedImage thành mảng byte
 	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
 	    ImageIO.write(bufferedImage, "png", baos);
 	    return baos.toByteArray();
 	}




}