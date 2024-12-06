package mks.myworkspace.cvhub.controller;
import java.util.ArrayList;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.controller.model.CvDTO;
import mks.myworkspace.cvhub.controller.model.JobRequestDTO;
import mks.myworkspace.cvhub.controller.model.OrganizationDTO;
import mks.myworkspace.cvhub.controller.model.OrganizationReviewDTO;
import mks.myworkspace.cvhub.entity.JobApplication;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.JobRole;
import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.entity.OrganizationReview;
import mks.myworkspace.cvhub.repository.JobApplicationRepository;
import mks.myworkspace.cvhub.repository.JobRequestRepository;
import mks.myworkspace.cvhub.repository.OrganizationRepository;
import mks.myworkspace.cvhub.repository.OrganizationReviewRepository;
import mks.myworkspace.cvhub.service.JobApplicationService;
import mks.myworkspace.cvhub.service.JobRequestService;
import mks.myworkspace.cvhub.service.JobRoleService;
import mks.myworkspace.cvhub.service.LocationService;
import mks.myworkspace.cvhub.service.OrganizationReviewService;
import mks.myworkspace.cvhub.service.OrganizationService;
import mks.myworkspace.cvhub.service.UserService;

@Controller
@RequiredArgsConstructor
public class JobPostingController extends BaseController{
	@Autowired
	OrganizationService organizationService;
	@Autowired
	JobRequestRepository jobRequestRepository;
	@Autowired
	JobRoleService jobRoleService;
	@Autowired
	LocationService locationService;
	@Autowired
	JobRequestService jobRequestService;
	@Autowired
	private UserService userService;
	@Autowired
	private JobApplicationService jobApplicationService;
	@Autowired
	private JobApplicationRepository jobApplicationRepository;
	
	private final OrganizationReviewService reviewService;
	private final OrganizationRepository organizationRepo;
	
	public final Logger logger = LoggerFactory.getLogger(this.getClass());;
	
	@GetMapping("/jobPosting")
	public ModelAndView jobPostingPage(Model model, Authentication authentication) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
	        return new ModelAndView("redirect:/login");
	    }
	    
	    String userEmail = auth.getName();
	    User user = userService.findUserByEmail(userEmail);
	    Organization organization = organizationService.findByUserId(user.getId());
	    
	    if (organization == null) {
	        return new ModelAndView("redirect:/showRegister");
	    }
	    
	    List<JobRole> jobRoles = jobRoleService.getRepo().findAll();
        List<Location> locations = locationService.getRepo().findAll();
        
        // Thêm vào model để hiển thị trong form HTML
        model.addAttribute("jobRoles", jobRoles);
        model.addAttribute("locations", locations);
        model.addAttribute("organization", organization);
        
	    return new ModelAndView("jobPosting");
	}
	
	@RequestMapping(value = "/registerJob1", method = RequestMethod.POST)
		public ModelAndView registerJob(@ModelAttribute JobRequestDTO jobRequestDTO) {
			System.out.println("Job Description: " + jobRequestDTO.getJobDescription());
		    System.out.println("Title: " + jobRequestDTO.getTitle());
		    System.out.println("Location Code: " + jobRequestDTO.getLocationCode());
		    System.out.println("Job Role ID: " + jobRequestDTO.getJobRoleId());
		    System.out.println("Experience: " + jobRequestDTO.getExperience());
		    System.out.println("Salary: " + jobRequestDTO.getSalary());
		    System.out.println("Organization ID: " + jobRequestDTO.getOrganizationId());
		    System.out.println("Requirements Candidate: " + jobRequestDTO.getRequirementsCandidate());
		    System.out.println("Benefit Candidate: " + jobRequestDTO.getBenefitCandidate());
		    System.out.println("Deadline Application: " + jobRequestDTO.getDeadlineApplication());
		    try {
		        // Xử lý logic tạo mới công việc
		        JobRequest jobRequest = jobRequestService.createJobRequest(
		            jobRequestDTO.getTitle(),
		            jobRequestDTO.getLocationCode(),
		            jobRequestDTO.getJobRoleId(),
		            jobRequestDTO.getExperience(),
		            jobRequestDTO.getSalary(),
		            jobRequestDTO.getOrganizationId(),
		            jobRequestDTO.getJobDescription(),
		            jobRequestDTO.getRequirementsCandidate(),
		            jobRequestDTO.getBenefitCandidate(),
		            jobRequestDTO.getDeadlineApplication()
		        );
		        jobRequest = jobRequestService.getRepo().save(jobRequest);
		        
		        // Redirect sau khi thêm thành công
		        ModelAndView mav = new ModelAndView();
		        mav.setViewName("redirect:/jobPosting");
		        return mav;
		    } catch (Exception e) {
		        // Trường hợp lỗi
		        ModelAndView mav = new ModelAndView("error");
		        mav.addObject("errorMessage", "Có lỗi xảy ra khi đăng ký công việc: " + e.getMessage());
		        return mav;
		    }
	}



}
