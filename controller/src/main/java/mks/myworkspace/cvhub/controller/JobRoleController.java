package mks.myworkspace.cvhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mks.myworkspace.cvhub.controller.model.JobRoleDTO;
import mks.myworkspace.cvhub.entity.JobRole;
import mks.myworkspace.cvhub.model.JobRoleJDBC;
import mks.myworkspace.cvhub.service.JobRoleService;

@Controller
@RequestMapping("/jobroles")
public class JobRoleController {

	@Autowired
	private JobRoleService jobRoleService;

	@GetMapping("")
	public ModelAndView getAllJobRoles(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, Model model) {

		Pageable pageable = PageRequest.of(page, size);
		Page<JobRole> jobRolesPage = jobRoleService.getAllJobRole(pageable);

		ModelAndView mav = new ModelAndView("jobRoles");
		mav.addObject("currentPage", page);
		mav.addObject("totalPages", jobRolesPage.getTotalPages());
		mav.addObject("jobroles", jobRolesPage.getContent());
		
		// Truyền thông báo nếu có
        String mess = (String) model.asMap().get("mess");
        model.addAttribute("mess", mess);

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

	//@PostMapping("/add")
	public String addJobRole(@ModelAttribute JobRoleDTO jobDTO, RedirectAttributes re) {
		if (jobRoleService.existsByTitle(jobDTO.getTitle())) {
		    re.addFlashAttribute("mess", "Title already exists");
		    return "redirect:/jobroles/add";
		}

		JobRole job = jobRoleService.createJobRole(jobDTO.getTitle(), jobDTO.getDescription());
		job = jobRoleService.getRepo().save(job);
		re.addFlashAttribute("mess", "Add jobrole thành công");
		
		return "redirect:/jobroles";
	}
	 
	//Dùng JDBC
	@PostMapping("/add")
    public String addJobRoleJdbc(@ModelAttribute JobRoleDTO jobDTO, RedirectAttributes re) {
        if (jobRoleService.existsByTitle(jobDTO.getTitle())) {
            re.addFlashAttribute("mess", "Title already exists");
            return "redirect:/jobroles/add";
        }

        JobRoleJDBC jobRoleJDBC = new JobRoleJDBC(jobDTO.getTitle(), jobDTO.getDescription());
        jobRoleJDBC = jobRoleService.createJobRoleJdbc(jobRoleJDBC);
        re.addFlashAttribute("mess", "Add jobrole thành công");

        return "redirect:/jobroles";
    }

	/* @GetMapping("/delete/{id}") */
	public String deleteJobRole(@PathVariable Long id, RedirectAttributes re) {
		JobRole job = jobRoleService.getRepo().findById(id).orElse(null);
		if (job != null) {
			// Xóa JobRole
			jobRoleService.deleteJobRole(job);
			re.addFlashAttribute("mess", "Xóa jobrole thành công");
			
			return "redirect:/jobroles";
		} else {
			// Nếu không tìm thấy JobRole
			re.addFlashAttribute("mess", "Not found jobrole with id: "+ id);
			
		    return "redirect:/jobroles";
		}
	}
	
	//Dùng JDBC
	@GetMapping("/delete/{id}")
	public String deleteJobRoleJdbc(@PathVariable Long id, RedirectAttributes re) {
		JobRoleJDBC job = jobRoleService.getJobRoleById(id);
	    if (job != null) {
	        // Delete JobRole using JDBC
	        jobRoleService.deleteJobRoleJdbc(job);
	        re.addFlashAttribute("mess", "Xóa jobrole thành công");
	        
	        return "redirect:/jobroles";
	    } else {
	        // If JobRole not found
	        re.addFlashAttribute("mess", "Not found jobrole with id: " + id);
	        
	        return "redirect:/jobroles";
	    }
	}

	/* @PostMapping("/edit/{id}") */
	public String updateJobRole(@PathVariable Long id, @ModelAttribute JobRoleDTO jobDTO, RedirectAttributes re) {

		JobRole job = jobRoleService.getRepo().findById(id).orElse(null);
		if (job != null) {
			String title = jobDTO.getTitle();

			// neu trung title thi set thong bao
			if (jobRoleService.canEditByTitle(title)) {
				JobRole updateJob = jobRoleService.updateJobRole(job, jobDTO.getTitle(), jobDTO.getDescription());
				re.addFlashAttribute("mess", "Updater jobrole thành công");

				return "redirect:/jobroles";
			} else {

				re.addFlashAttribute("mess", "Title already exists or not exists, please try again");

				return "redirect:/jobroles/edit/" + id;
			}

		}
		else {
			re.addFlashAttribute("mess", "not found jobrole with id: " + id);
			
			return "redirect:/jobroles";
		}
	}
	
	//Dùng JDBC
	@PostMapping("/edit/{id}")
	public String updateJobRole1(@PathVariable Long id, @ModelAttribute JobRoleDTO jobDTO, RedirectAttributes re) {
		JobRoleJDBC jobRoleJDBC = jobRoleService.getJobRoleById(id);
	    if (jobRoleJDBC != null) {
	        String title = jobDTO.getTitle();

	        if (jobRoleService.canEditByTitle(title)) {
	            jobRoleJDBC.setTitle(title);
	            jobRoleJDBC.setDescription(jobDTO.getDescription());
	            jobRoleService.updateJobRoleJdbc(jobRoleJDBC);
	            re.addFlashAttribute("mess", "Update jobrole thành công");

	            return "redirect:/jobroles";
	        } else {
	            re.addFlashAttribute("mess", "Title already exists or not exists, please try again");

	            return "redirect:/jobroles/edit/" + id;
	        }
	    } else {
	        re.addFlashAttribute("mess", "not found jobrole with id: " + id);

	        return "redirect:/jobroles";
	    }
	}
}
