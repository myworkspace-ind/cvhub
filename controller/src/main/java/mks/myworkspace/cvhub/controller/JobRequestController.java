package mks.myworkspace.cvhub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.cvhub.controller.model.JobRequestDTO;
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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Controller
@RequestMapping("/jobrequests")
public class JobRequestController {
	@Autowired
	JobRequestService jobRequestService;
	@Autowired
	OrganizationService organizationService;
	@Autowired
	JobRoleService jobRoleService;
	@Autowired
	LocationService locationService;

	@GetMapping("") // http://localhost:8080/cvhub-web/jobrequests?page=0&limit=10
	public ModelAndView getAllJobRoles(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit) {
		ModelAndView mav = new ModelAndView("home");
		PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdDate").descending());
		Page<JobRequest> jobRequestPage = jobRequestService.getRepo().findAll(pageRequest);
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
		JobRequest jobRequest = jobRequestService.getRepo().findById(id).orElse(null);
		mav.addObject("jobRequest", jobRequest);
		List<JobRole> alLJobRole= jobRoleService.getRepo().findAll();
		List<Location> locations = locationService.getRepo().findAll();
		mav.addObject("locations", locations);
		mav.addObject("alLJobRole", alLJobRole);
		return mav;
	}
	@RequestMapping(value = { "/registerJob" }, method = RequestMethod.POST)
    public ModelAndView registerJob(@ModelAttribute JobRequestDTO jobRequestDTO) {
		 System.out.println("Job Description: " + jobRequestDTO.getJobDescription());
        try {
            JobRequest jobRequest = jobRequestService.createJobRequest(
            		jobRequestDTO.getTitle(),
            		jobRequestDTO.getLocationCode(),
            		jobRequestDTO.getJobRoleId(),
            		jobRequestDTO.getExperience(),
            		jobRequestDTO.getSalary(),
            		jobRequestDTO.getOrganizationId(),
            		jobRequestDTO.getJobDescription(),
            		jobRequestDTO.getRequirementsCandidate(),
            		jobRequestDTO.getBenefitCandidate(),
            		jobRequestDTO.getDeadlineApplication()
            		);
            jobRequest = jobRequestService.getRepo().save(jobRequest);
            ModelAndView mav = new ModelAndView();
	        mav.setViewName("redirect:/organization?id=" + jobRequestDTO.getOrganizationId());
	        return mav;
        } catch (Exception e) {
            ModelAndView mav = new ModelAndView("error");
            mav.addObject("errorMessage", "Có lỗi xảy ra khi đăng ký công việc: " + e.getMessage());
            return mav;
        }
    }
	@RequestMapping(value = "/updateJob/{id}", method = RequestMethod.POST)
	public ModelAndView updateJob(@PathVariable Long id,@ModelAttribute JobRequestDTO jobRequestDTO) {
	    try {
	    	JobRequest jobRequest = jobRequestService.getRepo().findById(id).orElse(null);
	        // Update the existing job request
	        JobRequest updatedJobRequest = jobRequestService.updateJobRequest(
	        	jobRequest,
	            jobRequestDTO.getTitle(),
	            jobRequestDTO.getLocationCode(),
	            jobRequestDTO.getJobRoleId(),
	            jobRequestDTO.getExperience(),
	            jobRequestDTO.getSalary(),
	            jobRequestDTO.getJobDescription(),
	            jobRequestDTO.getRequirementsCandidate(),
	            jobRequestDTO.getBenefitCandidate(),
	            jobRequestDTO.getDeadlineApplication(),
	            jobRequestDTO.getCourseLink()
	        );

	        ModelAndView mav = new ModelAndView();
	        mav.setViewName("redirect:/organization?id=" + updatedJobRequest.getOrganization().getId());
	        return mav;
	    } catch (Exception e) {
	        ModelAndView mav = new ModelAndView("error");
	        mav.addObject("errorMessage", "Có lỗi xảy ra khi cập nhật thông tin công việc: " + e.getMessage());
	        return mav;
	    }
	}
	@RequestMapping(value = "/deleteJob/{id}", method = RequestMethod.POST)
	public ModelAndView deleteJob(@PathVariable Long id) {
	    ModelAndView mav = new ModelAndView();
	    try {
	        // Tìm kiếm JobRequest theo id
	        JobRequest jobRequest = jobRequestService.getRepo().findById(id).orElse(null);
	        
	        if (jobRequest != null) {
	            // Xóa JobRequest
	            jobRequestService.deleteJobRequest(jobRequest);
	            mav.setViewName("redirect:/organization?id=" + jobRequest.getOrganization().getId());
	        } else {
	            // Nếu không tìm thấy JobRequest
	            mav.setViewName("error");
	            mav.addObject("errorMessage", "Không tìm thấy thông tin công việc để xóa.");
	        }
	    } catch (Exception e) {
	        // Xử lý lỗi
	        mav.setViewName("error");
	        mav.addObject("errorMessage", "Có lỗi xảy ra khi xóa thông tin công việc: " + e.getMessage());
	    }
	    return mav;
	}
}