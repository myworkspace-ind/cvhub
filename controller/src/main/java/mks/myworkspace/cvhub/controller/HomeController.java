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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.cvhub.entity.JobRole;
import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.service.JobRoleService;
import mks.myworkspace.cvhub.service.LocationService;

/**
 * Handles requests for the application home page.
 */
@Controller

public class HomeController extends BaseController {

	@Autowired
	JobRoleService jobRoleService;
	@Autowired
	LocationService locationService;

	/**
	 * This method is called when binding the HTTP parameter to bean (or model).
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// Sample init of Custom Editor

//        Class<List<ItemKine>> collectionType = (Class<List<ItemKine>>)(Class<?>)List.class;
//        PropertyEditor orderNoteEditor = new MotionRuleEditor(collectionType);
//        binder.registerCustomEditor((Class<List<ItemKine>>)(Class<?>)List.class, orderNoteEditor);

	}

	/**
	 * Simply selects the home view to render by returning its name.
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
	public ModelAndView displayHome(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("home");

		initSession(request, httpSession);

		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());
		return mav;
	}

	@RequestMapping(value = { "/add" }, method = RequestMethod.GET)
	public ModelAndView addJobRoles() {
		ModelAndView mav = new ModelAndView("searchResult");
		List<Location> locations = locationService.getRepo().findAll();
		List<JobRole> jobRoles = Arrays.asList(new JobRole(1L, "Java Developer", locations.get(0), "IT"),
				new JobRole(2L, "Marketing Specialist", locations.get(0), "Marketing"),
				new JobRole(3L, "Financial Analyst", locations.get(0), "Finance"),
				new JobRole(4L, "Registered Nurse", locations.get(0), "Healthcare"),
				new JobRole(5L, "Civil Engineer", locations.get(0), "Construction"),
				new JobRole(6L, "Software Engineer", locations.get(0), "IT"),
				new JobRole(7L, "Data Scientist", locations.get(0), "IT"),
				new JobRole(8L, "Project Manager", locations.get(0), "Construction"),
				new JobRole(9L, "Marketing Manager", locations.get(0), "Marketing"),
				new JobRole(10L, "Accountant", locations.get(0), "Finance"),
				new JobRole(11L, "Teacher", locations.get(0), "Education"),
				new JobRole(12L, "Physician", locations.get(0), "Healthcare"));
		jobRoleService.getRepo().saveAll(jobRoles);

		return mav;
	}

	@RequestMapping(value = { "/addLocation" }, method = RequestMethod.GET)
	public ModelAndView addLocation() {
		ModelAndView mav = new ModelAndView("searchResult");
		List<Location> locations = locationService.fetchLocationsFromAPI();

		locationService.getRepo().saveAll(locations);

		return mav;
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView searchJobs(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "location", required = false, defaultValue = "0") int locationCode,
			@RequestParam(value = "industry", required = false, defaultValue = "") String industry,
			HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("searchResult");

		// 1. Trước tiên, lọc theo ngành
		List<JobRole> jobs = jobRoleService.getRepo().findByIndustry(industry);

		// 2. Tiếp tục lọc kết quả dựa trên các tiêu chí khác
		List<JobRole> searchResults = jobs.stream()
				.filter(job -> job.getTitle().toLowerCase().contains(keyword.toLowerCase())
						&& (locationCode == 0 || job.getLocation().getCode() == locationCode))
				.collect(Collectors.toList());

		mav.addObject("searchResults", searchResults);
		mav.addObject("keyword", keyword);
		mav.addObject("location", locationCode);
		mav.addObject("industry", industry);

		return mav;
	}
}
