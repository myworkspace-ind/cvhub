package mks.myworkspace.cvhub.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.cvhub.controller.model.JobSearchDTO;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.repository.JobRequestRepository;
import mks.myworkspace.cvhub.service.JobRoleService;
import mks.myworkspace.cvhub.service.LocationService;
import mks.myworkspace.cvhub.service.OrganizationService;
import mks.myworkspace.cvhub.service.SearchJobService;

@Controller

@Slf4j
public class phuhao extends BaseController{

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

	public final Logger logger = LoggerFactory.getLogger(this.getClass());;
	
//	@RequestMapping(value = "/search", method = RequestMethod.GET)
//	public ModelAndView searchJobs(@ModelAttribute JobSearchDTO jobSearchDTO, HttpServletRequest request,
//								   HttpSession httpSession) {
//		ModelAndView mav = new ModelAndView("searchResult");
//		List<JobRequest> searchResults = searchjobService.searchJobRequest(jobSearchDTO.getKeyword(),
//				jobSearchDTO.getLocation(), jobSearchDTO.getIndustry());
//		mav.addObject("searchResults", searchResults);
//		return mav;
//	}
	@RequestMapping(value = { "/searchJob_hao/job" }, method = RequestMethod.GET)	
	public ModelAndView search(@ModelAttribute JobSearchDTO jobSearchDTO,HttpServletRequest request,
			HttpSession httpSession) {
			ModelAndView mav;
			
		
			String requestURI = request.getRequestURI();			
			String viewName = "searchJob_hao";  // Nếu là /searchJob, trả về view searchJob
						
			mav = new ModelAndView(viewName);	// Trả về dữ liệu chung cho cả 2 trang
			
			initSession(request, httpSession);
			
		;
		
			List<JobRequest> searchResults = searchjobService.searchJobRequest(jobSearchDTO.getKeyword(),
					jobSearchDTO.getLocation(), jobSearchDTO.getIndustry());
			mav.addObject("jobrequests", searchResults);					
			mav.addObject("currentSiteId", getCurrentSiteId());
			mav.addObject("userDisplayName", getCurrentUserDisplayName());
			List<Location> locations = locationService.getRepo().findAll();
			mav.addObject("locations", locations);
			return mav;
			}
	@RequestMapping(value = { "/searchJob_hao" }, method = RequestMethod.GET)	
	public ModelAndView displayHome(HttpServletRequest request,
			HttpSession httpSession,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value ="limit", defaultValue = "12") int limit) {
			ModelAndView mav;
					
			String requestURI = request.getRequestURI();			
			String viewName = "searchJob_hao";  // Nếu là /searchJob, trả về view searchJob
						
			mav = new ModelAndView(viewName);	// Trả về dữ liệu chung cho cả 2 trang
			
			initSession(request, httpSession);
			PageRequest pageRequest = PageRequest.of(
			page, limit,
			Sort.by("createdDate").descending()
			);
			Page<JobRequest> jobRequestPage = jobRequestRepository.findAll(pageRequest);
			int totalPages = jobRequestPage.getTotalPages();
			List<JobRequest> jobRequests = jobRequestPage.getContent();
			mav.addObject("jobrequests", jobRequests);
			mav.addObject("totalPages", totalPages);
			mav.addObject("currentPage", page);
			mav.addObject("currentSiteId", getCurrentSiteId());
			mav.addObject("userDisplayName", getCurrentUserDisplayName());
			List<Location> locations = locationService.getRepo().findAll();
			mav.addObject("locations", locations);
			return mav;
			}
}
