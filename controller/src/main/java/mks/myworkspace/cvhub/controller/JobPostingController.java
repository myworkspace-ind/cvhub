package mks.myworkspace.cvhub.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.controller.model.CvDTO;
import mks.myworkspace.cvhub.controller.model.JobRequestDTO;
import mks.myworkspace.cvhub.controller.model.OrganizationDTO;
import mks.myworkspace.cvhub.controller.model.OrganizationReviewDTO;
import mks.myworkspace.cvhub.entity.JobApplication;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.JobRole;
import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.entity.OrganizationReview;
import mks.myworkspace.cvhub.repository.JobApplicationRepository;
import mks.myworkspace.cvhub.repository.JobRequestRepository;
import mks.myworkspace.cvhub.repository.OrganizationRepository;
import mks.myworkspace.cvhub.repository.OrganizationReviewRepository;
import mks.myworkspace.cvhub.service.JobApplicationService;
import mks.myworkspace.cvhub.service.JobRequestService;
import mks.myworkspace.cvhub.service.JobRoleService;
import mks.myworkspace.cvhub.service.LocationService;
import mks.myworkspace.cvhub.service.OrganizationReviewService;
import mks.myworkspace.cvhub.service.OrganizationService;
import mks.myworkspace.cvhub.service.UserService;

@Controller
@RequiredArgsConstructor
public class JobPostingController extends BaseController {
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
	@Autowired
	private UserService userService;
	@Autowired
	private JobApplicationService jobApplicationService;
	@Autowired
	private JobApplicationRepository jobApplicationRepository;

	private final OrganizationReviewService reviewService;
	private final OrganizationRepository organizationRepo;

	public final Logger logger = LoggerFactory.getLogger(this.getClass());;

	@GetMapping("/jobPosting")
	public ModelAndView jobPostingPage(Model model, Authentication authentication) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
			return new ModelAndView("redirect:/login");
		}

		String userEmail = auth.getName();
		User user = userService.findUserByEmail(userEmail);
		Organization organization = organizationService.findByUserId(user.getId());

		if (organization == null) {
			return new ModelAndView("redirect:/showRegister");
		}

		List<JobRole> jobRoles = jobRoleService.getRepo().findAll();
		List<Location> locations = locationService.getRepo().findAll();

		// Thêm vào model để hiển thị trong form HTML
		model.addAttribute("jobRoles", jobRoles);
		model.addAttribute("locations", locations);
		model.addAttribute("organization", organization);

		return new ModelAndView("jobPosting");
	}

	@RequestMapping(value = "/registerJob1", method = RequestMethod.POST)
	public ModelAndView registerJob(@ModelAttribute JobRequestDTO jobRequestDTO) {
		try {
			// Xử lý logic tạo mới công việc
			JobRequest jobRequest = jobRequestService.createJobRequest(jobRequestDTO.getTitle(),
					jobRequestDTO.getLocationCode(), jobRequestDTO.getJobRoleId(), jobRequestDTO.getExperience(),
					jobRequestDTO.getSalary(), jobRequestDTO.getOrganizationId(), jobRequestDTO.getJobDescription(),
					jobRequestDTO.getRequirementsCandidate(), jobRequestDTO.getBenefitCandidate(),
					jobRequestDTO.getDeadlineApplication());
			jobRequest = jobRequestService.getRepo().save(jobRequest);

			// Redirect sau khi thêm thành công
			ModelAndView mav = new ModelAndView();
			mav.setViewName("redirect:/jobPosting");
			return mav;
		} catch (Exception e) {
			// Trường hợp lỗi
			ModelAndView mav = new ModelAndView("error");
			mav.addObject("errorMessage", "Có lỗi xảy ra khi đăng ký công việc: " + e.getMessage());
			return mav;
		}
	}

	@GetMapping("/showJob")
	public ModelAndView showJob(Model model, Authentication authentication) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
			return new ModelAndView("redirect:/login");
		}

		String userEmail = auth.getName();
		User user = userService.findUserByEmail(userEmail);
		Organization organization = organizationService.findByUserId(user.getId());

		if (organization == null) {
			return new ModelAndView("redirect:/showRegister");
		}

		List<JobRequest> listJob = jobRequestService.findAllByOrganizationId(organization.getId());
		
	    List<String> locationNames = new ArrayList<>();
	    for (JobRequest jobRequest : listJob) {
	        locationNames.add(jobRequest.getLocation().getName());  // Thêm tên Location vào danh sách
	    }
	    model.addAttribute("locationNames", locationNames);
		model.addAttribute("listJob", listJob);

		return new ModelAndView("showJob");
	}
	
	@GetMapping("/deleteJob/{id}")
	public String deleteJob(@PathVariable("id") Long id) {
	    jobRequestRepository.deleteById(id);
	    return "redirect:/showJob";
	}


	@GetMapping("/editJob/{id}")
	public ModelAndView editJob(Model model, Authentication authentication, @PathVariable("id") Long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
			return new ModelAndView("redirect:/login");
		}

		String userEmail = auth.getName();
		User user = userService.findUserByEmail(userEmail);
		Organization organization = organizationService.findByUserId(user.getId());

		if (organization == null) {
			return new ModelAndView("redirect:/showRegister");
		}

		// Lấy thông tin công việc từ database
        JobRequest existingJobRequest = jobRequestService.getRepo().findById(id)
                .orElseThrow(() -> new IllegalStateException("JobRequest not found"));
        List<JobRole> jobRoles = jobRoleService.getRepo().findAll();
		List<Location> locations = locationService.getRepo().findAll();

		// Thêm vào model để hiển thị trong form HTML
		model.addAttribute("jobRoles", jobRoles);
		model.addAttribute("locations", locations);
		model.addAttribute("organization", organization);
		model.addAttribute("jobRequest", existingJobRequest);
		
		return new ModelAndView("updateJob");
	}

	@PostMapping("/updateJob")
    public ModelAndView updateJob(@RequestParam("id") Long id,
                            @RequestParam("title") String title,
                            @RequestParam("jobRoleId") Long jobRoleId,
                            @RequestParam("locationCode") int locationCode,
                            @RequestParam("jobDescription") String jobDescription,
                            @RequestParam("experience") int experience,
                            @RequestParam("salaryOption") int salaryOption,
                            @RequestParam(value = "salary", required = false) int salary,
                            @RequestParam("requirementsCandidate") String requirementsCandidate,
                            @RequestParam("benefitCandidate") String benefitCandidate,
                            @RequestParam("deadlineApplication") String deadlineApplication) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        String userEmail = auth.getName();
	        User user = userService.findUserByEmail(userEmail);
	        Organization organization = organizationService.findByUserId(user.getId());
	        ModelAndView mav = new ModelAndView();
	        
	        if (organization == null) {
	            throw new IllegalStateException("Organization không tìm thấy");
	        }
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        LocalDate deadline = LocalDate.parse(deadlineApplication, formatter);
			JobRole jobRole = jobRoleService.getRepo().getById(jobRoleId);
			Location location = locationService.getRepo().getById(locationCode);
			// Xử lý logic tạo mới công việc
			JobRequest jobRequest = new JobRequest();
			jobRequest.setId(id);
			jobRequest.setTitle(title);
	        jobRequest.setLocation(location);
	        jobRequest.setJobRole(jobRole);
	        jobRequest.setExperience(experience);
	        jobRequest.setSalary(salary);
	        jobRequest.setDetailsJob(jobDescription);
	        jobRequest.setRequirementsCandidate(requirementsCandidate);
	        jobRequest.setOrganization(organization);
	        jobRequest.setBenefitCandidate(benefitCandidate);
	        jobRequest.setDeadlineApplication(deadline);

	        // Lưu đối tượng vào database
	        jobRequestService.getRepo().save(jobRequest);
	        mav.setViewName("redirect:/showJob");
			return mav;	
		} catch (Exception e) {
			ModelAndView mav = new ModelAndView("error");
			mav.addObject("errorMessage", "Có lỗi xảy ra khi đăng ký công việc: " + e.getMessage());
			return mav;
		}
    }
}
