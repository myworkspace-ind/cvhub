package mks.myworkspace.cvhub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.repository.JobRequestRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Controller
@RequestMapping("/jobrequests")
public class JobRequestController {

    @Autowired
    private JobRequestRepository jobRequestRepository;
	
	@GetMapping("")  //http://localhost:8080/cvhub-web/jobrequests?page=0&limit=10
	public ModelAndView getAllJobRoles(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value ="limit", defaultValue = "10") int limit )
    {
		ModelAndView mav = new ModelAndView("home");
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
        return mav;
    }

    @GetMapping("/{id}")
    public ModelAndView getDetailJob(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("jobDetail");
        JobRequest jobRequest = jobRequestRepository.findById(id).orElse(null);
        mav.addObject("jobRequest", jobRequest);
        return mav;
    }
}