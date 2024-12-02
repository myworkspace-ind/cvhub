package mks.myworkspace.cvhub.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.repository.JobRequestRepository;
import mks.myworkspace.cvhub.service.FolderUserService;
import mks.myworkspace.cvhub.service.JobRequestService;
import mks.myworkspace.cvhub.service.JobRoleService;
import mks.myworkspace.cvhub.service.LocationService;
import mks.myworkspace.cvhub.service.OrganizationService;
import mks.myworkspace.cvhub.service.SearchJobService;

/**
 * 
 */
@Controller
@Slf4j
public class pageKhanhNguyenController extends BaseController{

	@Autowired
	OrganizationService organizationService;
	@Autowired
	JobRoleService jobRoleService;
	@Autowired
	LocationService locationService;
	@Autowired
	SearchJobService searchjobService;
	@Autowired
    private JobRequestRepository jobRequestRepository;
	@Autowired
	FolderUserService fileService;
	
	@Autowired
	JobRequestService jobRequestService;
	
	 @Autowired
	 private FolderUserService fileSystemService;
	
	@GetMapping("/jobrequests_kn")
	public ModelAndView getAllJobRoles(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "9") int limit) {
		ModelAndView mav = new ModelAndView("searchJob");
		PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdDate").descending());
		Page<JobRequest> jobRequestPage = jobRequestService.getRepo().findAll(pageRequest);

		int totalPages = jobRequestPage.getTotalPages();

		List<JobRequest> jobRequests = jobRequestPage.getContent();
		mav.addObject("jobrequests", jobRequests);
		mav.addObject("totalPages", totalPages);
		mav.addObject("currentPage", page);
		mav.addObject("limit", limit);
		
		return mav;
	}

}
