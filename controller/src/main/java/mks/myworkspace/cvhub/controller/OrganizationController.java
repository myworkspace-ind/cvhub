package mks.myworkspace.cvhub.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import mks.myworkspace.cvhub.controller.model.CvDTO;
import mks.myworkspace.cvhub.controller.model.JobRequestDTO;
import mks.myworkspace.cvhub.controller.model.JobSearchDTO;
import mks.myworkspace.cvhub.controller.model.OrganizationDTO;
import mks.myworkspace.cvhub.controller.model.OrganizationReviewDTO;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.JobRole;
import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.entity.OrganizationReview;
import mks.myworkspace.cvhub.repository.JobRequestRepository;
import mks.myworkspace.cvhub.repository.OrganizationRepository;
import mks.myworkspace.cvhub.repository.OrganizationReviewRepository;
import mks.myworkspace.cvhub.service.JobRequestService;
import mks.myworkspace.cvhub.service.JobRoleService;
import mks.myworkspace.cvhub.service.LocationService;
import mks.myworkspace.cvhub.service.OrganizationReviewService;
import mks.myworkspace.cvhub.service.OrganizationService;

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
	
	private final OrganizationReviewService reviewService;
	private final OrganizationRepository organizationRepo;
	
	public final Logger logger = LoggerFactory.getLogger(this.getClass());;
	@RequestMapping(value = { "/organization" }, method = RequestMethod.GET)
	public ModelAndView displayHome(@RequestParam("id") Long id,HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("organizationDetails");
		Organization organization = organizationService.getRepo().findById(id).orElse(null);
		List<JobRequest> jobByOrganization = jobRequestRepository.findByOrganizationId(id);
		List<JobRole> alLJobRole= jobRoleService.getRepo().findAll();
		List<Location> locations = locationService.getRepo().findAll();
		List<OrganizationReview> reviews = reviewService.getReviewsByOrganizationId(id);
		mav.addObject("locations", locations);
		mav.addObject("alLJobRole", alLJobRole);
		mav.addObject("organization", organization);
		mav.addObject("jobByOrganization", jobByOrganization);
		mav.addObject("reviews", reviews);
		mav.addObject("reviewCount", reviews.size());
		return mav;
	}
	@RequestMapping(value = { "/showRegister" }, method = RequestMethod.GET)
		public ModelAndView registerOrganization( HttpServletRequest request,
				HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("organization/register");
		 List<JobRole> jobRoles = jobRoleService.getRepo().findAll();
		
		 mav.addObject("jobRoles", jobRoles);
			List<Location> locations = locationService.getRepo().findAll();
			mav.addObject("locations", locations);
		return mav;
	}
	@RequestMapping(value = { "/registerOrganization" }, method = RequestMethod.POST)
	public ModelAndView registerOrganization(@ModelAttribute OrganizationDTO organizationDTO, 
	                                         HttpServletRequest request,
	                                         HttpSession httpSession) {
	    try {
	        // Tạo và lưu tổ chức
	        Organization organization = organizationService.createOrganization(
	            organizationDTO.getTitle(),
	            organizationDTO.getLogoFile(),
	            organizationDTO.getWebsite(),
	            organizationDTO.getSummary(),
	            organizationDTO.getDetail(),
	            organizationDTO.getLocation()
	        );
	        organization = organizationService.getRepo().save(organization);
	        // Chuyển hướng đến trang tổ chức
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
	@RequestMapping(value = { "/organizations" }, method = RequestMethod.GET)
	public ModelAndView getOrganizations(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("candidate/organizationList");
		List<Organization> organizations = organizationService.getRepo().findAll();
		mav.addObject("organizations", organizations);
		return mav;
	}
	@RequestMapping(value = { "/organizations/{id}/addReview" }, method = RequestMethod.POST)
	public OrganizationReview addReview(@PathVariable("id") Long organizationId,
										@RequestBody OrganizationReviewDTO reviewDTO,
										HttpServletRequest request, 
										HttpSession httpSession) 
	{
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
	
	@RequestMapping(value = { "/organization/{id}/getCVs" }, method = RequestMethod.GET)
	public ModelAndView GetCVs(@ModelAttribute OrganizationDTO organizationDTO, 
											 @PathVariable("id") long id, 
	                                         HttpServletRequest request,
	                                         HttpSession httpSession) 
	{
	    try {
	        List<JobRequest> jobRequests = jobRequestService.getRepo().findByOrganizationId(id);
	        // Chuyển hướng đến trang tổ chức
	        ModelAndView mav = new ModelAndView("organization/getCVs.html");
	        CvDTO cv1 = new CvDTO();
	        CvDTO cv2 = new CvDTO();
	        CvDTO cv3 = new CvDTO();
	        
	        cv1.setEmail("cv1@gmail.com");
	        cv1.setEducation("ute1");
	        cv1.setFullName("Nguyen Van A");
	        cv1.setPhone("0123456789");
	        cv1.setLocationCode(0);
	        cv1.setSkills("Java");
	        cv1.setExperience("3");

	        cv2.setEmail("cv2@gmail.com");
	        cv2.setEducation("FPT University");
	        cv2.setFullName("Nguyen Van B");
	        cv2.setPhone("0987654321");
	        cv2.setLocationCode(1);
	        cv2.setSkills("Python, Machine Learning");
	        cv2.setExperience("2");

	        cv3.setEmail("cv3@gmail.com");
	        cv3.setEducation("HCMUS");
	        cv3.setFullName("Tran Thi B");
	        cv3.setPhone("0369852147");
	        cv3.setLocationCode(2);
	        cv3.setSkills("ReactJS, NodeJS, MongoDB");
	        cv3.setExperience("4");
	        
	        List<CvDTO> cvs = new ArrayList();
	        cvs.add(cv1);
	        cvs.add(cv2);
	        cvs.add(cv3);
	        
	        mav.addObject("jobRequests", jobRequests);
	        mav.addObject("cvs", cvs);
	        
	        return mav;

	    } catch (Exception e) {
	        // Xử lý lỗi
	        ModelAndView mav = new ModelAndView("error");
	        mav.addObject("errorMessage", "Có lỗi xảy ra khi đăng ký tổ chức: " + e.getMessage());
	        return mav;
	    }
	}
	
}package mks.myworkspace.cvhub.controller;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.controller.model.OrganizationDTO;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.JobRole;
import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.repository.JobRequestRepository;
import mks.myworkspace.cvhub.service.JobRequestService;
import mks.myworkspace.cvhub.service.JobRoleService;
import mks.myworkspace.cvhub.service.LocationService;
import mks.myworkspace.cvhub.service.OrganizationService;
import mks.myworkspace.cvhub.service.UserService;

@Controller
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
	public final Logger logger = LoggerFactory.getLogger(this.getClass());;
	 @RequestMapping(value = { "/organization/{id}" }, method = RequestMethod.GET)
	    public ModelAndView displayHome(@PathVariable("id") Long id, HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("organizationDetails");
		Organization organization = organizationService.getRepo().findById(id).orElse(null);
		List<JobRequest> jobByOrganization = jobRequestRepository.findByOrganizationId(id);
		List<JobRole> alLJobRole= jobRoleService.getRepo().findAll();
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
        
        
        mav.addObject("jobRoles", jobRoles);
        mav.addObject("locations", locations);
        return mav;
    }
	 @RequestMapping(value = { "/registerOrganization" }, method = RequestMethod.POST)
	    public ModelAndView registerOrganization(@ModelAttribute OrganizationDTO organizationDTO, 
	                                           HttpServletRequest request,
	                                           HttpSession httpSession) {
	        try {
	            // Get current logged in user
	            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	            User currentUser = userService.findUserByEmail(auth.getName());
	            
	            // Create and save organization
	            Organization organization = organizationService.createOrganization(
	                organizationDTO.getTitle(),
	                organizationDTO.getLogoFile(),
	                organizationDTO.getWebsite(),
	                organizationDTO.getSummary(),
	                organizationDTO.getDetail(),
	                organizationDTO.getLocation()
	            );
	            
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
	            ModelAndView mav = new ModelAndView("error");
	            mav.addObject("errorMessage", "Có lỗi xảy ra khi đăng ký tổ chức: " + e.getMessage());
	            return mav;
	        }
	    }
	
}