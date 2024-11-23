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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
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

    @GetMapping("/jobrequestsbytime/filter")
    public ModelAndView filterJobRequests(@RequestParam String timePeriod,
                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "limit", defaultValue = "10") int limit) {
        ModelAndView mav = new ModelAndView("report_duy");
        LocalDateTime startDate;

        switch (timePeriod) {
            case "today":
                startDate = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
                break;
            case "week":
                startDate = LocalDateTime.now().minusDays(LocalDateTime.now().getDayOfWeek().getValue() - 1)
                        .truncatedTo(ChronoUnit.DAYS);
                break;
            case "month":
                startDate = LocalDateTime.now().withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
                break;
            default:
                startDate = LocalDateTime.MIN;
        }

        Date startDateAsDate = java.sql.Timestamp.valueOf(startDate);

        Pageable pageRequest = PageRequest.of(page, limit, Sort.by("createdDate").descending());
        // Lấy dữ liệu từ repository
        Page<JobRequest> jobRequestPage = jobRequestRepository.findByCreatedDateAfter(startDateAsDate, pageRequest);

// In ra console để kiểm tra
        System.out.println("===== Job Requests Fetched =====");
        jobRequestPage.getContent().forEach(job -> {
            System.out.println("ID: " + job.getId());
            System.out.println("Title: " + job.getTitle());
            System.out.println("Created Date: " + job.getCreatedDate());
            System.out.println("Organization: " + job.getOrganization().getTitle());
            System.out.println("Location: " + job.getLocation().getName());
            System.out.println("Job Role: " + job.getJobRole().getTitle());
            System.out.println("----------------------------------");
        });


        mav.addObject("jobrequests", jobRequestPage.getContent());
        mav.addObject("totalPages", jobRequestPage.getTotalPages());
        mav.addObject("currentPage", page);
        mav.addObject("timePeriod", timePeriod);
        return mav;
    }


}
