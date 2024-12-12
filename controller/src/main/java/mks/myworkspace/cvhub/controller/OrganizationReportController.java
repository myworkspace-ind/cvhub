package mks.myworkspace.cvhub.controller;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.cvhub.controller.model.JobApplicationSummaryDTO;
import mks.myworkspace.cvhub.controller.model.JobRequestSummaryDTO;
import mks.myworkspace.cvhub.controller.model.OrganizationSummaryDTO;
import mks.myworkspace.cvhub.entity.JobApplication;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.repository.JobApplicationRepository;
import mks.myworkspace.cvhub.service.JobApplicationService;
import mks.myworkspace.cvhub.service.JobRequestService;
import mks.myworkspace.cvhub.service.OrganizationService;
import mks.myworkspace.cvhub.service.impl.JobRequestImpl;

@Controller
public class OrganizationReportController {

	@Autowired
	OrganizationService organizationService;
	@Autowired
	JobRequestService jobRequestService;
	@Autowired
	JobApplicationRepository jobApplicationRepos;

	public static Date getStartDate(String period) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		switch (period.toLowerCase()) {
		case "this_week":
			calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
			return calendar.getTime();

		case "this_month":
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			return calendar.getTime();

		case "this_year":
			calendar.set(Calendar.DAY_OF_YEAR, 1);
			return calendar.getTime();
		case "last_year":
			calendar.add(Calendar.YEAR, -1);
		    calendar.set(Calendar.DAY_OF_YEAR, 1);
			return calendar.getTime();

		default:	// "today"
			return calendar.getTime();
		}
	}
	
	@GetMapping("/report/org/jobRequests")
	@ResponseBody
	public List<JobRequestSummaryDTO> getJobsByOrgId(@RequestParam Long orgId) {
	    return jobRequestService.findAllByOrganizationId(orgId).stream().map(jr -> {
	    	return new JobRequestSummaryDTO(
	    		jr.getId(),
	    		jr.getTitle(),
	    		jr.getLocation().getName(),
	    		jr.getExperience(),
	    		jr.getSalary()
	    	);
	    }).collect(Collectors.toList());
	}
	
	@GetMapping("/report/org/jobApplications")
	@ResponseBody
	public List<JobApplicationSummaryDTO> getApplicantsByOrgId(@RequestParam Long orgId) {
	    return jobApplicationRepos.findApplicantByOrgId(orgId).stream().map(ja -> {
	    	return new JobApplicationSummaryDTO(
	    		ja.getJobRequest().getId(),
	    		ja.getJobRequest().getTitle(),
	    		ja.getUser().getFullName(),
	    		ja.getUser().getEmail(),
	    		ja.getUser().getPhone()
	    	);
	    }).collect(Collectors.toList());
	}

	@GetMapping("/report/organization")
	public ModelAndView showOrganizationReport(
			@RequestParam(value = "timePeriod", defaultValue = "today") String period,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit) {

		// filter
		Date startDate = getStartDate(period);

		// pagination
		PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdDate").descending());
		Page<Organization> orgRequestPage = organizationService.getRepo().findAllCreatedDateStartFrom(startDate, pageRequest);
		int totalPages = orgRequestPage.getTotalPages();

		// objects
		List<OrganizationSummaryDTO> organizationDTOs = orgRequestPage.getContent().stream().map(org -> {
			return new OrganizationSummaryDTO(
				org.getId(),
				org.getLogoID(),
				org.getLogo(),
				org.getWebsite(),
				org.getTitle(),
				org.getDetail(),
				org.getLocation(),
				org.getCreatedDate(),
				jobRequestService.getRepo().getJobCountByOrgId(org.getId()),
				jobApplicationRepos.getApplicantCountByOrgId(org.getId())
			);
		}).collect(Collectors.toList());
		
		// view
		ModelAndView mav = new ModelAndView("organizationReport");
		mav.addObject("period", period);
		mav.addObject("limit", limit);
		mav.addObject("currentPage", page);
		mav.addObject("totalPages", totalPages);
		mav.addObject("organizations", organizationDTOs);

		return mav;
	}
}