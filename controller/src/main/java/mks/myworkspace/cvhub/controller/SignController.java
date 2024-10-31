package mks.myworkspace.cvhub.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mks.myworkspace.cvhub.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mks.myworkspace.cvhub.controller.model.UserDTO;
import mks.myworkspace.cvhub.service.UserService;

@Controller
public class SignController extends BaseController {
	@Autowired
    private UserService userService;
	@RequestMapping(value = { "user/showregister" }, method = RequestMethod.GET)
	public ModelAndView registerUser(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("/signInOut/signup");
		return mav;
	}

	@RequestMapping(value = { "/register" }, method = RequestMethod.POST)
	public ModelAndView registerUser(@ModelAttribute UserDTO userDTO, BindingResult result,
			RedirectAttributes redirectAttributes) {
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
			User user = userService.createUser(userDTO.getFullName(), userDTO.getEmail(), userDTO.getPassword(), userDTO.getConfirmPassword(), userDTO.getPhone());

// Redirect to login with success message
			mav.setViewName("redirect:/login");
			redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");

		} catch (Exception e) {
			mav.setViewName("/signInOut/signup");
			mav.addObject("error", e.getMessage());
		}

		return mav;
	}
}