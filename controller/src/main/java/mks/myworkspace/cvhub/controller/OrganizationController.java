package mks.myworkspace.cvhub.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.cvhub.controller.model.JobRequestDTO;
import mks.myworkspace.cvhub.controller.model.JobSearchDTO;
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
	public final Logger logger = LoggerFactory.getLogger(this.getClass());;
	@RequestMapping(value = { "/organization" }, method = RequestMethod.GET)
	public ModelAndView displayHome(@RequestParam("id") Long id,HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("organizationDetails");
		Organization organization = organizationService.getRepo().findById(id).orElse(null);
		List<JobRequest> jobByOrganization = jobRequestRepository.findByOrganizationId(id);
		List<JobRole> alLJobRole= jobRoleService.getRepo().findAll();
		List<Location> locations = locationService.getRepo().findAll();
		mav.addObject("locations", locations);
		mav.addObject("alLJobRole", alLJobRole);
		mav.addObject("organization", organization);
		mav.addObject("jobByOrganization", jobByOrganization);
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
	@RequestMapping(value = { "/organizations/addReview" }, method = RequestMethod.POST)
	public ModelAndView addReview(@ModelAttribute OrganizationDTO organizationDTO, HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("candidate/organizationList");
		List<Organization> organizations = organizationService.getRepo().findAll();
		mav.addObject("organizations", organizations);
		return mav;
	}
	
}