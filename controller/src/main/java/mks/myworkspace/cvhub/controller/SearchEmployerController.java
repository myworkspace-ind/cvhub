package mks.myworkspace.cvhub.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.repository.OrganizationRepository;
import mks.myworkspace.cvhub.service.OrganizationExportExcelService;
import mks.myworkspace.cvhub.service.OrganizationService;


@Controller
public class SearchEmployerController extends BaseController{

	@Autowired
    OrganizationRepository organizationRepository;
	
    @Autowired
    OrganizationService organizationService;
    
    @Autowired
    OrganizationExportExcelService exportService;
    
    @GetMapping("/resources")
    public ModelAndView getEmployers(HttpServletRequest request,
						    		HttpSession httpSession, 
						    		@RequestParam(value = "page", defaultValue = "0") int page,
						            @RequestParam(value ="limit", defaultValue = "10") int limit){
    	ModelAndView mav = new ModelAndView("employer");
    	
    	initSession(request, httpSession);
		PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("createdDate").descending()
        );
//    	List<Organization> employers = organizationService.getRepo().findAll();
        Page<Organization> employersPage = organizationRepository.findAll(pageRequest);
        int totalPages = employersPage.getTotalPages();
        List<Organization> employers = employersPage.getContent();
        
        mav.addObject("totalPages", totalPages);
        mav.addObject("currentPage", page);
        mav.addObject("limit", limit);
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());
    	mav.addObject("employers", employers);
    	
    	return mav;
    }
    
    @RequestMapping(value = "/search-employer", method = RequestMethod.GET)
	public ModelAndView searchJobs( HttpServletRequest request,
									HttpSession httpSession, 
									@RequestParam String keyword,
									@RequestParam(value = "page", defaultValue = "0") int page,
							        @RequestParam(value ="limit", defaultValue = "10") int limit) {
		ModelAndView mav = new ModelAndView("searchEmployer");
		
//		List<Organization> searchResults = organizationService.findByTitleContaining(keyword);
		
		PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("createdDate").descending()
        );
		
		Page<Organization> employersPage = organizationRepository.findByTitleContaining(pageRequest, keyword);
        int totalPages = employersPage.getTotalPages();
        List<Organization> searchResult = employersPage.getContent();
        
        mav.addObject("totalPages", totalPages);
        mav.addObject("currentPage", page);
        mav.addObject("limit", limit);
        mav.addObject("keyword", keyword);
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());
    	mav.addObject("employers", searchResult);

		return mav;
	}
}
