package mks.myworkspace.cvhub.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import mks.myworkspace.cvhub.entity.*;
import mks.myworkspace.cvhub.service.*;

@Controller
@RequestMapping("/api/v1/jobroles")
public class JobRoleController extends BaseController {
	@Autowired
	JobRoleService jobRoleService;
	
	@GetMapping("")  //http://localhost:8080/cvhub-web/api/v1/jobroles?page=0&limit=10
	public ModelAndView getAllJobRoles(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value ="limit", defaultValue = "10") int limit )
    {
		ModelAndView mav = new ModelAndView("home");
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("createdDate").descending()
        );
        Page<JobRole> jobRolePage = jobRoleService.getRepo().findAll(pageRequest);
        int totalPages = jobRolePage.getTotalPages();
        List<JobRole> jobRoles = jobRolePage.getContent();
        mav.addObject("jobroles", jobRoles);
        mav.addObject("totalPages", totalPages);
        mav.addObject("currentPage", page);
        return mav;
    }
	
}
