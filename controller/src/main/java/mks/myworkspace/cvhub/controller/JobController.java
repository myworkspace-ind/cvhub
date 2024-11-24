package mks.myworkspace.cvhub.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.repository.JobRequestRepository;
import mks.myworkspace.cvhub.service.LocationService;

@Controller
@RequestMapping("/job")
public class JobController extends BaseController {
	@Autowired
    private JobRequestRepository jobRequestRepository;

    @Autowired
    private LocationService locationService;

    @GetMapping
    public ModelAndView displayHome(HttpServletRequest request,
            HttpSession httpSession,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value ="limit", defaultValue = "10") int limit) {

        ModelAndView mav = new ModelAndView("job_thanh_22110420"); // Chỉ trả về nội dung của trang job

        // Phần còn lại của code xử lý dữ liệu
        initSession(request, httpSession);
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdDate").descending());
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
