package mks.myworkspace.cvhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.service.PhoneNumberFormatService;
import mks.myworkspace.cvhub.service.UserService;

@Controller
public class ReportUserController {
	@Autowired
    private UserService userService;

    @Autowired
	private PhoneNumberFormatService phoneNumberFormatService;

    /**
     * Display user list with filter options (today, this week, this month, this year).
     *
     * @param period Filter period (today, week, month, year).
     * @param page   Current page for pagination.
     * @param limit  Number of records per page.
     * @return ModelAndView to render the view.
     */
    @GetMapping("/report/user")
    public ModelAndView displayUsers(
            @RequestParam(value = "period", defaultValue = "today") String period,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit
    ) {
        ModelAndView mav = new ModelAndView("reportUser");

        // Create pageable object with sorting by createdDate
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdDate").descending());

        // Retrieve users based on the selected period
        Page<User> userPage = userService.findUsersByPeriod(period, pageable);

        // Format the phone numbers for each user
        for (User user : userPage.getContent()) {
            user.setPhone(phoneNumberFormatService.formatPhoneNumber(user.getPhone()));  // Format phone number
        }

        // Add data to the model
        mav.addObject("users", userPage.getContent());
        mav.addObject("totalPages", userPage.getTotalPages());
        mav.addObject("currentPage", page);
        mav.addObject("selectedPeriod", period);
        mav.addObject("limit", limit);

        return mav;
    }
    
    /**
     * Delete a user by ID.
     *
     * @param id User ID to delete.
     * @return Redirect to the user report page.
     */
    @PostMapping("/report/user/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/report/user"; // Redirect back to the user report page
    }
    @GetMapping("/thongke")
    public ModelAndView getUserReport(@RequestParam(value = "year", defaultValue = "2024") int year) {
        List<Long> userCounts = userService.getUserCountsPerYear(year);  // Lấy danh sách số lượng người dùng theo từng tháng trong năm
        
        ModelAndView mav = new ModelAndView("thongke");
        mav.addObject("userCounts", userCounts);  // Truyền số lượng người dùng
        mav.addObject("year", year);  // Truyền năm để hiển thị trong view
        return mav;
    }
}
