package mks.myworkspace.cvhub.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mks.myworkspace.cvhub.entity.CV;
import mks.myworkspace.cvhub.entity.JobApplication;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.repository.CvRepository;
import mks.myworkspace.cvhub.repository.JobApplicationRepository;
import mks.myworkspace.cvhub.repository.UserRepository;
import mks.myworkspace.cvhub.service.CvService;

@Controller
public class EmployeeController {

	@Autowired
	CvService cvService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CvRepository cvRepository;
	
	@Autowired
	JobApplicationRepository jobApplicationRepository;
	
	@GetMapping("/employee")
    public ModelAndView getEmployees(
            @RequestParam(defaultValue = "0") int page, // trang mặc định là 0
            @RequestParam(defaultValue = "10") int size // kích thước mặc định là 10
    ) {
        ModelAndView mav = new ModelAndView("employee-list");

        Pageable pageable = PageRequest.of(page, size); // Tạo Pageable từ page và size
        //Page<User> cvPage = userRepository.findUsersWithCVs(pageable);
        Page<User> cvPage = userRepository.findAll(pageable);

        mav.addObject("employees", cvPage.getContent()); // Truyền danh sách CV vào
        mav.addObject("currentPage", page); // Trang hiện tại
        mav.addObject("totalPages", cvPage.getTotalPages()); // Tổng số trang
        return mav;
    }
	
	@GetMapping("/employee/detail")
    public ModelAndView EmployeDetails(@RequestParam("id") Long userId) {
        ModelAndView mav = new ModelAndView("employee-detail");

        User user = userRepository.findById(userId).orElse(null);
        List<CV> userCVs = cvService.findCVsByUserId(userId);
        List<JobApplication> jobApplications = jobApplicationRepository.findByUser(user);
        
        // Nhóm jobApplications theo công ty
        Map<String, List<JobRequest>> companyJobsMap = jobApplications.stream()
            .collect(Collectors.groupingBy(
                ja -> ja.getJobRequest().getOrganization().getTitle(), // Lấy tên công ty
                Collectors.mapping(JobApplication::getJobRequest, Collectors.toList()) // Lấy danh sách công việc
            ));
        
        mav.addObject("user", user);
        mav.addObject("cvList", userCVs);
        mav.addObject("jobApp", jobApplications);
        mav.addObject("companyJobsMap", companyJobsMap);
        return mav;
    }
	
	@PostMapping("/employee-search")
    public ModelAndView searchEmployee(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
		ModelAndView mav = new ModelAndView("employee-list");
		
        Pageable pageable = PageRequest.of(page, size);
        Page<User> cvPage = userRepository.search(keyword, pageable);

        mav.addObject("employees", cvPage.getContent()); // Truyền danh sách CV vào
        mav.addObject("currentPage", page); // Trang hiện tại
        mav.addObject("totalPages", cvPage.getTotalPages()); // Tổng số trang
        mav.addObject("keyword", keyword);
        return mav;
    }
	
	/**
     * Xóa CV theo ID.
     * @param id ID của CV cần xóa.
     * @param redirectAttributes Dùng để thêm thông báo khi xóa.
     * @return Redirect về trang chi tiết nhân viên hoặc danh sách nhân viên.
     */
    @GetMapping("/employee/detail/delete-cv/{id}")
    public String deleteCV(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
    	User employee = userRepository.findUserByCVId(id).orElse(null);
        cvRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa CV thành công!");
        return "redirect:/employee/detail?id=" + employee.getId();
    }
}
