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

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.cvhub.entity.Organization;
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
	@GetMapping("/main")
	public ModelAndView displayMain(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("main");

		initSession(request, httpSession);

		return mav;
	}

	/**
	 * Handles requests for the Report tab and Report's subtabs.
	 *
	 * @return
	 */
	@GetMapping("/report")
	public ModelAndView displayReport() {
		ModelAndView mav = new ModelAndView("main");

		mav.addObject("isSelectedReport", true);
		mav.addObject("activeSubtab", "jobs");
		mav.addObject("subtabView", "fragments/reportSubtabs/jobReport :: content");

		return mav;
	}

	public static Date getStartDate(String period) {
		Calendar calendar = Calendar.getInstance();

		switch (period.toLowerCase()) {
		case "today":
	        calendar.set(Calendar.HOUR_OF_DAY, 0);
	        calendar.set(Calendar.MINUTE, 0);
	        calendar.set(Calendar.SECOND, 0);
	        calendar.set(Calendar.MILLISECOND, 0);
	        return calendar.getTime();

		case "this_week":
			calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
	        calendar.set(Calendar.HOUR_OF_DAY, 0);
	        calendar.set(Calendar.MINUTE, 0);
	        calendar.set(Calendar.SECOND, 0);
	        calendar.set(Calendar.MILLISECOND, 0);
	        return calendar.getTime();

		case "this_month":
			calendar.set(Calendar.DAY_OF_MONTH, 1);
	        calendar.set(Calendar.HOUR_OF_DAY, 0);
	        calendar.set(Calendar.MINUTE, 0);
	        calendar.set(Calendar.SECOND, 0);
	        calendar.set(Calendar.MILLISECOND, 0);
	        return calendar.getTime();

		case "this_year":
			calendar.set(Calendar.DAY_OF_YEAR, 1);
	        calendar.set(Calendar.HOUR_OF_DAY, 0);
	        calendar.set(Calendar.MINUTE, 0);
	        calendar.set(Calendar.SECOND, 0);
	        calendar.set(Calendar.MILLISECOND, 0);
	        return calendar.getTime();

		default:
			throw new IllegalArgumentException("Invalid period: " + period);
		}
	}

	@GetMapping("/report/{subtab}")
	public ModelAndView handleSubtabs(@PathVariable String subtab,
			@RequestParam(value = "timePeriod", defaultValue = "today") String period,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "5") int limit) {

		ModelAndView mav = new ModelAndView("main");

		mav.addObject("isSelectedReport", true);
		mav.addObject("activeSubtab", subtab);

		switch (subtab) {
		case "applicants":
			mav.addObject("subtabView", "fragments/reportSubtabs/applicantReport :: content");
			break;
		case "users":
			mav.addObject("subtabView", "fragments/reportSubtabs/userReport :: content");
			break;
		case "organizations":
			mav.addObject("subtabView", "fragments/reportSubtabs/organizationReport :: content");

			Date startDate = getStartDate(period);
			PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdDate").descending());
			Page<Organization> orgRequestPage = organizationService.getRepo().findAllCreatedDateStartFrom(startDate, pageRequest);
			List<Organization> organizations = orgRequestPage.getContent();
			int totalPages = orgRequestPage.getTotalPages();
			
			mav.addObject("period", period);
			mav.addObject("organizations", organizations);
			mav.addObject("totalPages", totalPages);
			mav.addObject("currentPage", page);

			break;
		default:
			mav.addObject("subtabView", "fragments/reportSubtabs/jobReport :: content");
		}

		return mav;
	}
	
}
