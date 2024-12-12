package mks.myworkspace.cvhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.service.UserService;

@Controller
public class ReportUserController {
	@Autowired
    private UserService userService;

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

        // Add data to the model
        mav.addObject("users", userPage.getContent());
        mav.addObject("totalPages", userPage.getTotalPages());
        mav.addObject("currentPage", page);
        mav.addObject("selectedPeriod", period);
        mav.addObject("limit", limit);

        return mav;
    }
}
