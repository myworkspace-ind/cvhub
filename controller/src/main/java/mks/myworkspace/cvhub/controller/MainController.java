/**
 * Licensed to MKS Group under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * MKS Group licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package mks.myworkspace.cvhub.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.cvhub.controller.model.OrganizationDTO;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.service.LocationService;
import mks.myworkspace.cvhub.service.OrganizationService;

/**
 * Handles requests for the application home page.
 */
@Controller
@Slf4j
public class MainController extends BaseController {
	@Autowired
	OrganizationService organizationService;

	/**
	 * Handles requests for the application home page on Platform MyWorkspace/Sakai.
	 * 
	 * @return
	 */
	@Autowired
	LocationService locationService;

	@GetMapping("/main")
	public ModelAndView displayMain(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("main");

		initSession(request, httpSession);

		return mav;
	}
	@GetMapping("/main/user")
	public ModelAndView displayUser(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("user_profile");

		initSession(request, httpSession);

		return mav;
	}


	@RequestMapping(value = { "/main/report/organization" }, method = RequestMethod.GET)
	public ModelAndView displayReportOrganization(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, HttpServletRequest request, HttpSession httpSession) {

		ModelAndView mav = new ModelAndView("organization/organizationReport");
		Page<Organization> organizationPage = organizationService.getRepo().findAll(PageRequest.of(page, size));
		mav.addObject("organizations", organizationPage.getContent());
		mav.addObject("currentPage", page); // Trang hien tai
		mav.addObject("totalPages", organizationPage.getTotalPages()); // Tong so trang
		return mav;
	}
	

	@GetMapping("searchOrganization")
	  public ModelAndView searchOrganizations(@RequestParam(value = "companyName", required = false) String companyName, 
	 @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, HttpServletRequest request,
	  HttpSession httpSession) { ModelAndView mav = new ModelAndView("organization/organizationReport");

	 List<Organization>searchResults = organizationService.searchByTitle(companyName);
	 Page<Organization> organizationPage = new PageImpl<>(searchResults,PageRequest.of(page, size), searchResults.size()); // Them ket qua vao
	 mav.addObject("organizations", organizationPage.getContent());
	 mav.addObject("currentPage", page); // Trang hien tai
	 mav.addObject("totalPages", organizationPage.getTotalPages()); // Tong so trang
	 mav.addObject("size", size);  // Kich thuoc moi trang
	 return mav; }
	 
}
