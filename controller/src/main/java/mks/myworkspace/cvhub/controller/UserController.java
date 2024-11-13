package mks.myworkspace.cvhub.controller;

import java.net.http.HttpHeaders;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mks.myworkspace.cvhub.entity.CV;
import mks.myworkspace.cvhub.entity.JobApplication;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.controller.model.UserDTO;
import mks.myworkspace.cvhub.service.CvService;
import mks.myworkspace.cvhub.service.JobApplicationService;
import mks.myworkspace.cvhub.service.JobRequestService;
import mks.myworkspace.cvhub.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private CvService cvService;
    
    @Autowired
    private JobRequestService jobRequestService;
    
    @Autowired
    private JobApplicationService jobApplicationService;

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
    
}