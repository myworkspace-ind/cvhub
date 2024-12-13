package mks.myworkspace.cvhub.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.core.tools.picocli.CommandLine.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
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
public class OrganizationController extends BaseController {
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

	@RequestMapping(value = { "/organization/{id}" }, method = RequestMethod.GET)
	public ModelAndView displayHome(@PathVariable("id") Long id, HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("organizationDetails");
		Organization organization = organizationService.getRepo().findById(id).orElse(null);
		List<JobRequest> jobByOrganization = jobRequestRepository.findByOrganizationId(id);
		List<JobRole> alLJobRole = jobRoleService.getRepo().findAll();
		List<Location> locations = locationService.getRepo().findAll();
		mav.addObject("locations", locations);
		mav.addObject("alLJobRole", alLJobRole);
		mav.addObject("organization", organization);
		mav.addObject("jobByOrganization", jobByOrganization);
		boolean isOwner = false;
		if (request.getUserPrincipal() != null) {
			isOwner = organizationService.isOwner(id, request.getUserPrincipal().getName());
		}
		mav.addObject("isOwner", isOwner);

		return mav;
	}


	@RequestMapping(value = { "/organizations/{id}/addReview" }, method = RequestMethod.POST)
	public OrganizationReview addReview(@PathVariable("id") Long organizationId,
			@RequestBody OrganizationReviewDTO reviewDTO, HttpServletRequest request, HttpSession httpSession) {
//		ModelAndView mav = new ModelAndView("candidate/organizationList"); 
		OrganizationReview newReview = new OrganizationReview();
		newReview.setOrganization(organizationRepo.findById(organizationId).get());
		newReview.setRating(reviewDTO.getRating());
		newReview.setReviewText(reviewDTO.getReviewText());
		reviewService.createReview(newReview);
		return newReview;
//		mav.addObject("organizations", organizations);
//		return mav;
	}

	@RequestMapping(value = { "/organizations/{id}/getReviews" }, method = RequestMethod.POST)
	public List<OrganizationReview> getReviews(HttpServletRequest request, HttpSession httpSession) {
//		ModelAndView mav = new ModelAndView("candidate/organizationList"); 
		return reviewService.getReviews();
//		mav.addObject("organizations", organizations);
//		return mav;
	}
	@PreAuthorize("hasRole('ROLE_ADMIN') and @organizationService.isOwner(#id, principal.username)")
	@RequestMapping(value = { "/organization" }, method = RequestMethod.GET)
	public ModelAndView displayHomeForOrganization(HttpServletRequest request) {
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    User currentUser = userService.findUserByEmail(auth.getName());
		    Organization org = organizationService.findByUserId(currentUser.getId());
		    
		    if (org != null) {
		        return new ModelAndView("redirect:/organization/" + org.getId());
		    }
		    return new ModelAndView("redirect:/showRegister");
	}

	@RequestMapping(value = { "/registerOrganization" }, method = RequestMethod.POST)
    public ModelAndView registerOrganization(@ModelAttribute OrganizationDTO organizationDTO, 
                                           HttpServletRequest request,
                                           HttpSession httpSession) 
	{
		try {
			// Get current logged in user
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User currentUser = userService.findUserByEmail(auth.getName());

			// Create and save organization
			Organization organization = organizationService.createOrganization(organizationDTO.getTitle(),
					organizationDTO.getLogoFile(), organizationDTO.getWebsite(), organizationDTO.getSummary(),
					organizationDTO.getDetail(), organizationDTO.getLocation());

			// Link organization with user
			organization.setUser(currentUser);
			organization = organizationService.getRepo().save(organization);

			// Update user role to ROLE_ADMIN
			currentUser.setRole("ROLE_ADMIN");
			userService.getRepo().save(currentUser);

			// Redirect to organization page
			ModelAndView mav = new ModelAndView();
			mav.setViewName("redirect:/organization?id=" + organization.getId());
			return mav;

		} catch (Exception e) {
			// Xử lý lỗi
			ModelAndView mav = new ModelAndView("error");
			mav.addObject("errorMessage", "Có lỗi xảy ra khi đăng ký tổ chức: " + e.getMessage());
			return mav;
		}
	}
        @RequestMapping(value = { "showRegister" }, method = RequestMethod.GET)
        public ModelAndView registerOrganization(HttpServletRequest request) {
            // Kiểm tra user đã đăng nhập
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
                return new ModelAndView("redirect:/login");
            }
            
            // Lấy thông tin user hiện tại
            User currentUser = userService.findUserByEmail(auth.getName());
            
            // Kiểm tra user đã có organization chưa
            Organization existingOrg = organizationService.findByUserId(currentUser.getId());
            if (existingOrg != null) {
                return new ModelAndView("redirect:/organization?id=" + existingOrg.getId());
            }
            
            ModelAndView mav = new ModelAndView("organization/register");
            List<JobRole> jobRoles = jobRoleService.getRepo().findAll();
            List<Location> locations = locationService.getRepo().findAll();
            Boolean reg = true;
            
            mav.addObject("userName", currentUser.getFullName());
            mav.addObject("jobRoles", jobRoles);
            mav.addObject("locations", locations);
            mav.addObject("reg", reg);
            return mav;
        }

	@RequestMapping(value = { "/organization/{id}/getCVs" }, method = RequestMethod.GET)
	public ModelAndView GetCVs(@ModelAttribute OrganizationDTO organizationDTO, @PathVariable("id") long id,
			HttpServletRequest request, HttpSession httpSession) {
		try {
			List<JobRequest> jobRequests = jobRequestService.findAllByOrganizationId(id);
			// Chuyển hướng đến trang tổ chức
			ModelAndView mav = new ModelAndView("organization/getCVs.html");
			List<User> users = new ArrayList<User>();
			List<JobApplication> applications = jobApplicationService.findAll();
			List<JobApplication> applicationsNeed = new ArrayList<JobApplication>();
			for ( var application : applications){
				for (var jobRequest : jobRequests) {
					if (jobRequest.getId() == application.getJobRequest().getId()) {
						users.add(application.getUser());
						applicationsNeed.add(application);
					}
				}
			}
			List<JobApplication> applicationsNeed2 = this.removeDuplicatesManually2(applicationsNeed);
			List<User> users2 = this.removeDuplicatesManually(users);
//			List<CvDTO> cvs = new ArrayList();
//			cvs.add(cv1);
//			cvs.add(cv2);
//			cvs.add(cv3);

			mav.addObject("jobRequests", jobRequests);
			mav.addObject("jobApplications", applicationsNeed2);
			mav.addObject("cvs", users2);
			mav.addObject("organizationId", id);
			return mav;
		}
		catch (Exception e) {
			// Xử lý lỗi
			ModelAndView mav = new ModelAndView("error");
			mav.addObject("errorMessage", "Có lỗi xảy ra khi get cv: " + e.getMessage());
			return mav;
		}
	}
	
	public static List<User> removeDuplicatesManually(List<User> list) {
        List<User> result = new ArrayList<>();
        for (User item : list) {
            if (!result.contains(item)) {
                result.add(item);
            }
        }
        return result;
    }
	
	public static List<JobApplication> removeDuplicatesManually2(List<JobApplication> list) {
        List<JobApplication> result = new ArrayList<>();
        for (JobApplication item : list) {
            if (!result.contains(item)) {
                result.add(item);
            }
        }
        return result;
    }

	@RequestMapping(value = { "/{organizationId}/getCVs/setStatusDeny/{id}" }, method = RequestMethod.POST)
	public ModelAndView setStatus(@PathVariable("id") Long jobApplicationId, @PathVariable("organizationId") Long organizationId, 
								HttpServletRequest request, HttpSession httpSession) {
		JobApplication jobApplication = jobApplicationService.getApplicationsByJobApplicationId(jobApplicationId);
		jobApplication.setStatus("DENY");
		jobApplicationRepository.save(jobApplication);
		return new ModelAndView("redirect:/organization/" + organizationId + "/getCVs");
	}
	
	@RequestMapping(value = { "/{organizationId}/getCVs/setStatusApprove/{id}" }, method = RequestMethod.POST)
	public ModelAndView setStatusApprove(@PathVariable("id") Long jobApplicationId, @PathVariable("organizationId") Long organizationId, 
								HttpServletRequest request, HttpSession httpSession) {
		JobApplication jobApplication = jobApplicationService.getApplicationsByJobApplicationId(jobApplicationId);
		jobApplication.setStatus("APPROVED");
		jobApplicationRepository.save(jobApplication);
		return new ModelAndView("redirect:/organization/" + organizationId + "/getCVs");
	}
	
	@RequestMapping(value = { "/organization/{organizationId}/getCVs/option" }, method = RequestMethod.GET)
	public ModelAndView findJobApplicationByOption(@RequestParam String status, @PathVariable("organizationId") Long organizationId, 
								HttpServletRequest request, HttpSession httpSession)
	{
		List<JobApplication> jobApplications = new ArrayList<>();
		if (status.equals("ALL")) 
			jobApplications = jobApplicationService.findAll();
		else
			jobApplications = jobApplicationService.findJobApplicationByOption(organizationId, status);
		List<JobRequest> jobRequests = jobRequestService.findAllByOrganizationId(organizationId);
		ModelAndView mav = new ModelAndView("organization/getCVs.html");
		mav.addObject("jobRequests", jobRequests);
		mav.addObject("jobApplications", jobApplications);
		mav.addObject("organizationId", organizationId);
		return mav;
	}
	
	@RequestMapping(value = { "/organization/update/{id}" }, method = RequestMethod.GET)
	public ModelAndView updateOrganization(@PathVariable("id") Long id, HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("organization/register");
		Organization organization = organizationService.getRepo().findById(id).orElse(null);
		List<JobRequest> jobByOrganization = jobRequestRepository.findByOrganizationId(id);
		List<JobRole> alLJobRole = jobRoleService.getRepo().findAll();
		List<Location> locations = locationService.getRepo().findAll();
		mav.addObject("locations", locations);
		mav.addObject("alLJobRole", alLJobRole);
		mav.addObject("org", organization);
		mav.addObject("jobByOrganization", jobByOrganization);
		mav.addObject("update", "update");
		return mav;
	}
	
	@RequestMapping(value = { "/organization/update/{id}" }, method = RequestMethod.POST)
	public ModelAndView updateOrganization(@PathVariable("id") Long id,
			@ModelAttribute OrganizationDTO organizationDTO, 
            HttpServletRequest request,
            HttpSession httpSession) {
		Organization organization = organizationService.findByOrganizationId(id);
		Organization organizationUpdated = organizationService.updateOrganization(organization, organizationDTO.getTitle(),
				organizationDTO.getLogoFile(), organizationDTO.getWebsite(), organizationDTO.getSummary(),
				organizationDTO.getDetail(), organizationDTO.getLocation());
		
		organizationService.getRepo().save(organizationUpdated);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/organization?id=" + organization.getId());
		
		return mav;
	}
	//hien thị danh sach doanh nghiep
	@RequestMapping(value = "/organization/list", method = RequestMethod.GET)
	public ModelAndView listOrganizations() {
		ModelAndView mav = new ModelAndView("listbusiness");
		try {
		List<Organization> organizations = organizationService.getRepo().findAll(); 
		mav.addObject("organizations", organizations);
		} catch (Exception e) {
		mav.addObject("error", "An error occurred while fetching the organization list: " + e.getMessage());
		e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value = "/organization/{organizationId}/jobs", method = RequestMethod.GET)
	public ModelAndView getJobsByOrganization(@PathVariable Long organizationId) {
		ModelAndView mav = new ModelAndView("organizationJobs");
			try {
			// Xử lý danh sách việc làm cho doanh nghiệp với ID = organizationId
			List<JobRequest> jobRequests = jobRequestService.getRepo().findByOrganizationId(organizationId);
			mav.addObject("jobRequests", jobRequests);
			mav.addObject("organizationId", organizationId); // Đảm bảo thêm organizationId vào model
			} catch (Exception e) {
			mav.setViewName("error");
			mav.addObject("errorMessage", "Có lỗi xảy ra khi lấy danh sách việc làm: " + e.getMessage());
			e.printStackTrace();
			}
			return mav;
		}
}

