package mks.myworkspace.cvhub.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.service.OrganizationService;

@Controller
public class OrganizationReportController {

	@Autowired
	OrganizationService organizationService;

	public static Date getStartDate(String period) {
		Calendar calendar = Calendar.getInstance();

		switch (period.toLowerCase()) {
		case "today":
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			return calendar.getTime();

		case "this_week":
			calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			return calendar.getTime();

		case "this_month":
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			return calendar.getTime();

		case "this_year":
			calendar.set(Calendar.DAY_OF_YEAR, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			return calendar.getTime();

		default:
			throw new IllegalArgumentException("Invalid period: " + period);
		}
	}

	@GetMapping("/report/organization")
	public ModelAndView showOrganizationReport(
			@RequestParam(value = "timePeriod", defaultValue = "today") String period,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit) {

		ModelAndView mav = new ModelAndView("organizationReport");

		Date startDate = getStartDate(period);
		
		PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdDate").descending());
		Page<Organization> orgRequestPage = organizationService.getRepo().findAllCreatedDateStartFrom(startDate, pageRequest);
		List<Organization> organizations = orgRequestPage.getContent();
		int totalPages = orgRequestPage.getTotalPages();

		mav.addObject("period", period);
		mav.addObject("organizations", organizations);
		mav.addObject("totalPages", totalPages);
		mav.addObject("currentPage", page);
		//mav.addObject("limit", limit);

		return mav;
	}
}