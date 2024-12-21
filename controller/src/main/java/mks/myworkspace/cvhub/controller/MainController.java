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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.cvhub.controller.model.OrganizationDTO;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.JobRole;
import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.repository.JobRequestRepository;
import mks.myworkspace.cvhub.repository.OrganizationRepository;
import mks.myworkspace.cvhub.service.FollowedOrganizationService;
import mks.myworkspace.cvhub.service.JobRoleService;
import mks.myworkspace.cvhub.service.LocationService;
import mks.myworkspace.cvhub.service.OrganizationService;
import mks.myworkspace.cvhub.service.UserService;

/**
 * Handles requests for the application home page.
 */
@Controller
@Slf4j
public class MainController extends BaseController {
	@Autowired
	OrganizationService organizationService;
	@Autowired
	JobRequestRepository jobRequestRepository;
	@Autowired
	private UserService userService;
	@Autowired
	FollowedOrganizationService followedOrganizationService;

	/**
	 * Handles requests for the application home page on Platform MyWorkspace/Sakai.
	 * 
	 * @return
	 */
	@Autowired
	LocationService locationService;
	@Autowired
	JobRoleService jobRoleService;
	
	@Value("${api.googleMapsKey}")
    private String apiKey;

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
		// Tính tổng yêu cầu tuyển dụng cho từng tổ chức
		Map<Long, Long> totalJobRequestsMap = new HashMap<>();
		for (Organization organization : organizationPage.getContent()) {
			Long totalRequests = organizationService.getTotalJobRequestsByOrganizationId(organization.getId());
			totalJobRequestsMap.put(organization.getId(), totalRequests);
		}
		mav.addObject("organizations", organizationPage.getContent());
		mav.addObject("currentPage", page); // Trang hien tai
		mav.addObject("totalPages", organizationPage.getTotalPages()); // Tong so trang
		mav.addObject("jobCount", totalJobRequestsMap);
		return mav;
	}

	@GetMapping("searchOrganization")
	public ModelAndView searchOrganizations(@RequestParam(value = "companyName", required = false) String companyName,
			@RequestParam(value = "location", required = false) String location,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("organization/organizationReport");

		List<Organization> searchResults;
		if (companyName != null && !companyName.isEmpty() && !"0".equals(location) && !location.isEmpty()) {
			// Tìm kiếm theo tên công ty và địa điểm
			searchResults = organizationService.searchByTitleAndLocation(companyName, location);
		} else if (companyName != null && !companyName.isEmpty()) {
			// Tìm kiếm theo tên công ty
			searchResults = organizationService.searchByTitle(companyName);
		} else if (!"0".equals(location) && !location.isEmpty()) {
			// Tìm kiếm theo địa điểm
			searchResults = organizationService.searchByLocation(location);
		} else {
			// Nếu không có tham số tìm kiếm -> lấy tất cả
			searchResults = organizationService.getRepo().findAll();
		}

		Page<Organization> organizationPage = new PageImpl<>(searchResults, PageRequest.of(page, size),
				searchResults.size()); // Them ket qua vao
		Map<Long, Long> totalJobRequestsMap = new HashMap<>();
		for (Organization organization : organizationPage.getContent()) {
			Long totalRequests = organizationService.getTotalJobRequestsByOrganizationId(organization.getId());
			System.out.println("Organization ID: " + organization.getId() + ", Total Job Requests: " + totalRequests);
			totalJobRequestsMap.put(organization.getId(), totalRequests);
		}
		mav.addObject("organizations", organizationPage.getContent());
		mav.addObject("currentPage", page); // Trang hien tai
		mav.addObject("totalPages", organizationPage.getTotalPages()); // Tong so trang
		mav.addObject("size", size); // Kich thuoc moi trang
		mav.addObject("jobCount", totalJobRequestsMap);
		return mav;
	}

	@RequestMapping(value = { "/main/organization/{id}" }, method = RequestMethod.GET)
	public ModelAndView displayHome(@PathVariable("id") Long id, HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("organization/organizationDetails");
		Organization organization = organizationService.getRepo().findById(id).orElse(null);
		List<JobRequest> jobByOrganization = jobRequestRepository.findByOrganizationId(id);
		List<JobRole> alLJobRole = jobRoleService.getRepo().findAll();
		List<Location> locations = locationService.getRepo().findAll();
		mav.addObject("locations", locations);
		mav.addObject("alLJobRole", alLJobRole);
		mav.addObject("organization", organization);
		mav.addObject("jobByOrganization", jobByOrganization);
		
		mav.addObject("apiKey", apiKey); // Đảm bảo không công khai API key này

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.findUserByEmail(auth.getName());
		if (followedOrganizationService.hasFollowedOrganization(currentUser, organization)) {
			mav.addObject("isFollowing", true);
		} else {
			mav.addObject("isFollowing", false);
		}
		boolean isOwner = false;
		if (request.getUserPrincipal() != null) {
			isOwner = organizationService.isOwner(id, request.getUserPrincipal().getName());
		}
		mav.addObject("isOwner", isOwner);
		return mav;
	}

	@PostMapping("/follow")
	public String follow(@RequestParam("organizationId") Long organizationId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || !auth.isAuthenticated()) {
			return "redirect:/login-view";
		}

		User currentUser = userService.findUserByEmail(auth.getName());
		Organization organization = organizationService.findByOrganizationId(organizationId);

		if (followedOrganizationService.hasFollowedOrganization(currentUser, organization)) {
			followedOrganizationService.deleteByUserAndOrganization(currentUser, organization);
		} else {
			followedOrganizationService.AddFollowedOrganization(currentUser, organization);
		}

		return "redirect:/main/organization/" + organizationId; // Redirect về trang của tổ chức
	}

}
