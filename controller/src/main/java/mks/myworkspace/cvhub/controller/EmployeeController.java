package mks.myworkspace.cvhub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmployeeController {

	@GetMapping("/employee")
	public String EmployeeList() {
		return "employee-list";
	}
}
