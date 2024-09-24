package mks.myworkspace.cvhub.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.repository.JobRequestRepository;
import mks.myworkspace.cvhub.service.JobRequestService;
import mks.myworkspace.cvhub.service.JobRoleService;
import mks.myworkspace.cvhub.service.OrganizationService;

@Controller
public class OrganizationDetails extends BaseController {
	@Autowired
	OrganizationService organizationService;
	@Autowired
	JobRequestRepository jobRequestRepository;
	@RequestMapping(value = { "/organization" }, method = RequestMethod.GET)
	public ModelAndView displayHome(@RequestParam("id") Long id,HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("organizationDetails");
		Organization organization = organizationService.getRepo().findById(id).orElse(null);
		List<JobRequest> jobByOrganization = jobRequestRepository.findByOrganizationId(id);
		mav.addObject("organization", organization);
		mav.addObject("jobByOrganization", jobByOrganization);
		return mav;
	}
}