package mks.myworkspace.cvhub.controller;

import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.repository.JobRequestRepository;
import mks.myworkspace.cvhub.service.JobRoleService;
import mks.myworkspace.cvhub.service.LocationService;
import mks.myworkspace.cvhub.service.OrganizationService;
import mks.myworkspace.cvhub.service.SearchJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class Report_Duy22110295 extends BaseController{
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

    /**
     * This method is called when binding the HTTP parameter to bean (or model).
     *
     * @param binder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        // Sample init of Custom Editor

//        Class<List<ItemKine>> collectionType = (Class<List<ItemKine>>)(Class<?>)List.class;
//        PropertyEditor orderNoteEditor = new MotionRuleEditor(collectionType);
//        binder.registerCustomEditor((Class<List<ItemKine>>)(Class<?>)List.class, orderNoteEditor);

    }

    /**
     * Simply selects the home view to render by returning its name.
     *
     * @return
     */
    @RequestMapping(value = { "/report_duy" }, method = RequestMethod.GET)
    public ModelAndView displayHome(HttpServletRequest request,
                                    HttpSession httpSession,
                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value ="limit", defaultValue = "10") int limit) {
        ModelAndView mav = new ModelAndView("report_duy");

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
