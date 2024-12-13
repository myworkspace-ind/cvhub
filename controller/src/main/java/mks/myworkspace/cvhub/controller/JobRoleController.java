package mks.myworkspace.cvhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.cvhub.controller.model.JobRoleDTO;
import mks.myworkspace.cvhub.entity.JobRole;
import mks.myworkspace.cvhub.service.JobRoleService;

@Controller
@RequestMapping("/jobroles")
public class JobRoleController {

	@Autowired
	private JobRoleService jobRoleService;

	@GetMapping("")
	public ModelAndView getAllJobRoles(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size);
		Page<JobRole> jobRolesPage = jobRoleService.getAllJobRole(pageable);

		ModelAndView mav = new ModelAndView("jobRoles");
		mav.addObject("currentPage", page);
		mav.addObject("totalPages", jobRolesPage.getTotalPages());
		mav.addObject("jobroles", jobRolesPage.getContent());

		return mav;
	}

	// Show add job role form
	@GetMapping({ "/add", "/edit/{id}" })
	public ModelAndView showRegisterForm(@PathVariable(required = false) Long id) {
		ModelAndView mav = new ModelAndView("jobRole-add");
		String alert = "";
		if (id != null) {
			JobRole jobrole = jobRoleService.getRepo().findById(id).orElse(null);
			if (jobrole != null) {

				alert = "isedit";
				mav.addObject("jobrole", jobrole);
				mav.addObject("alert", alert);
			}
		}

		return mav;

	}

	@PostMapping("/add")
	public ModelAndView addJobRole(@ModelAttribute JobRoleDTO jobDTO) {
		ModelAndView mav = new ModelAndView();

		String title = jobDTO.getTitle();

		// neu trung title thi thong bao
		if (jobRoleService.checkExistTitle(title)) {
			mav.addObject("exception", "Add không thành công, title đã tồn tại trước đó: " + title);
			mav.addObject("url", "/add/");
			mav.setViewName("error");
		} else {
			JobRole job = jobRoleService.createJobRole(jobDTO.getTitle(), jobDTO.getDescription());
			job = jobRoleService.getRepo().save(job);
			mav.setViewName("redirect:/jobroles");
		}

		return mav;
	}

	@GetMapping("/delete/{id}")
	public ModelAndView deleteJobRole(@PathVariable Long id) {
		ModelAndView mav = new ModelAndView();

		JobRole job = jobRoleService.getRepo().findById(id).orElse(null);
		if (job != null) {
			// Xóa JobRole
			jobRoleService.deleteJobRole(job);
			mav.setViewName("redirect:/jobroles");
		} else {
			// Nếu không tìm thấy JobRole
			mav.addObject("exception", "Không tìm thấy thông tin job role để xóa.");
			mav.setViewName("error");
		}

		return mav;
	}

	@PostMapping("/edit/{id}")
	public ModelAndView updateJobRole(@PathVariable Long id, @ModelAttribute JobRoleDTO jobDTO) {
		ModelAndView mav = new ModelAndView();

		JobRole job = jobRoleService.getRepo().findById(id).orElse(null);
		if (job != null) {
			String title = jobDTO.getTitle();

			// neu trung title thi set thong bao
			if (jobRoleService.checkExistTitle(title)) {
				mav.addObject("exception", "Update không thành công, title đã tồn tại trước đó: " + title);
				mav.addObject("url", "/edit/" + id);
				mav.setViewName("error");
			} else {
				JobRole updateJob = jobRoleService.updateJobRole(job, jobDTO.getTitle(), jobDTO.getDescription());
				mav.setViewName("redirect:/jobroles");
			}

		}
		return mav;
	}
}
