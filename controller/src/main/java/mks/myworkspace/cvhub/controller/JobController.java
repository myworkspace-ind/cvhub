package mks.myworkspace.cvhub.controller;

import java.awt.PageAttributes.MediaType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.cvhub.entity.JobApplication;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.repository.JobApplicationRepository;
import mks.myworkspace.cvhub.repository.JobRequestRepository;
import mks.myworkspace.cvhub.service.LocationService;

@Controller
@RequestMapping("/job")
public class JobController extends BaseController {
	@Autowired
    private JobRequestRepository jobRequestRepository;

    @Autowired
    private LocationService locationService;
    @Autowired
    private JobApplicationRepository jobApplicationRepository;
    @GetMapping
    public ModelAndView displayHome(
            HttpServletRequest request,
            HttpSession httpSession,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "location", required = false) String location) {
        
        ModelAndView mav = new ModelAndView("job_thanh_22110420");
        initSession(request, httpSession);
        
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdDate").descending());
        Page<JobRequest> jobRequestPage;
        
        // Xử lý tìm kiếm
        if (keyword != null && !keyword.trim().isEmpty()) {
            jobRequestPage = jobRequestRepository.searchByKeyword(keyword.trim(), pageRequest);
        } 
        // Xử lý lọc
        else if (location != null && !location.trim().isEmpty()) {
            jobRequestPage = jobRequestRepository.findByLocationName(location.trim(), pageRequest);
        } 
        // Không có điều kiện
        else {
            jobRequestPage = jobRequestRepository.findAll(pageRequest);
        }

        Map<Long, Long> applicationCounts = new HashMap<>();
        for (JobRequest job : jobRequestPage.getContent()) {
            Long count = jobApplicationRepository.countByJobRequestId(job.getId());
            applicationCounts.put(job.getId(), count);
        }

        mav.addObject("jobrequests", jobRequestPage.getContent());
        mav.addObject("applicationCounts", applicationCounts);
        mav.addObject("totalPages", jobRequestPage.getTotalPages());
        mav.addObject("currentPage", page);
        mav.addObject("keyword", keyword);
        mav.addObject("selectedLocation", location);
        mav.addObject("locations", locationService.getRepo().findAll());
        
        return mav;
    }
    @GetMapping("/applicants/{jobRequestId}")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<List<Map<String, String>>> getApplicantsByJobRequest(@PathVariable Long jobRequestId) {
        List<JobApplication> applications = jobApplicationRepository.findByJobRequestId(jobRequestId);
        List<Map<String, String>> applicantDetails = applications.stream().map(app -> {
            Map<String, String> details = new HashMap<>();
            details.put("name", app.getUser().getFullName());
            details.put("email", app.getUser().getEmail());
            details.put("phone", app.getUser().getPhone());
            details.put("status", app.getStatus());
            return details; 
        }).collect(Collectors.toList());

        return ResponseEntity.ok(applicantDetails);
    }
    

}
