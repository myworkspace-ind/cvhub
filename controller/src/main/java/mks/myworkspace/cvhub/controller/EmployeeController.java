package mks.myworkspace.cvhub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.cvhub.entity.CV;
import mks.myworkspace.cvhub.service.CvService;

@Controller
public class EmployeeController {

	@Autowired
	CvService cvService;
	
	@GetMapping("/employee")
    public ModelAndView getEmployees(
            @RequestParam(defaultValue = "0") int page, // trang mặc định là 0
            @RequestParam(defaultValue = "10") int size // kích thước mặc định là 10
    ) {
        ModelAndView mav = new ModelAndView("employee-list");

        Pageable pageable = PageRequest.of(page, size); // Tạo Pageable từ page và size
        Page<CV> cvPage = cvService.getPaginatedCVs(pageable);

        mav.addObject("employees", cvPage.getContent()); // Truyền danh sách CV vào
        mav.addObject("currentPage", page); // Trang hiện tại
        mav.addObject("totalPages", cvPage.getTotalPages()); // Tổng số trang
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
        Page<CV> cvPage = cvService.searchCVs(keyword, pageable);

        mav.addObject("employees", cvPage.getContent()); // Truyền danh sách CV vào
        mav.addObject("currentPage", page); // Trang hiện tại
        mav.addObject("totalPages", cvPage.getTotalPages()); // Tổng số trang
        mav.addObject("keyword", keyword);
        return mav;
    }
}
