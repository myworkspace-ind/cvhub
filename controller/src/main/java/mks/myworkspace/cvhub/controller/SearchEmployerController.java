package mks.myworkspace.cvhub.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.service.OrganizationService;

@Controller
public class SearchEmployerController {

    @Autowired
    OrganizationService organizationService;
    
    @GetMapping("/resources")
    public ModelAndView getEmployers(HttpServletRequest request, HttpSession httpSession){
    	ModelAndView mav = new ModelAndView("employer");
    	
    	List<Organization> employers = organizationService.getRepo().findAll();
    	
    	mav.addObject("employers", employers);
    	
    	return mav;
    }
    
    @RequestMapping(value = "/search-employer", method = RequestMethod.GET)
	public ModelAndView searchJobs(@RequestParam String keyword, HttpServletRequest request,
			HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("employer");
		
		List<Organization> searchResults = organizationService.findByTitleContaining(keyword);
		
		System.out.println(searchResults.size());
		mav.addObject("employers", searchResults);

		return mav;
	}
    
    
}
