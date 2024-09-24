package mks.myworkspace.cvhub.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.cvhub.entity.Location;

@Controller
public class OrganizationDetails extends BaseController {
	@RequestMapping(value = { "/organization" }, method = RequestMethod.GET)
	public ModelAndView displayHome(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("organizationDetails");
		return mav;
	}
}