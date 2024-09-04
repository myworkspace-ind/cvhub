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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import mks.myworkspace.cvhub.model.Job;
import mks.myworkspace.cvhub.model.Location;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController extends BaseController {
 
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
     * @return 
	 */
	@RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
	public ModelAndView displayHome(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("home");

		initSession(request, httpSession);
		
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());
		return mav;
	}

	 @RequestMapping(value = "/search", method = RequestMethod.GET)
	    public ModelAndView searchJobs(
	        @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
	        @RequestParam(value = "location", required = false, defaultValue = "0") int locationCode, // Sử dụng int cho mã tỉnh thành
	        @RequestParam(value = "industry", required = false, defaultValue = "") String industry,
	        HttpServletRequest request, 
	        HttpSession httpSession
	    ) {  
		 ModelAndView mav = new ModelAndView("searchResult");
	    List<Job> jobs = getDemoJobList();

        // 2. Thực hiện lọc kết quả 
	    List<Job> searchResults = jobs.stream()
	            .filter(job -> 
	                job.getTitle().toLowerCase().contains(keyword.toLowerCase()) &&
	                (locationCode == 0 || job.getLocation().getCode() == locationCode) &&
	                (industry.isBlank() || job.getIndustry().equalsIgnoreCase(industry))
	            )
	            .collect(Collectors.toList());
        mav.addObject("searchResults", searchResults);
        mav.addObject("keyword", keyword);
        mav.addObject("location",locationCode);
        mav.addObject("industry", industry); 
	 return mav;
}
	 private List<Job> getDemoJobList() {
		    // 1. Gọi API để lấy danh sách Location
		    List<Location> locations = fetchLocationsFromAPI(); // Giả sử hàm này đã được định nghĩa

		    // 2. Xử lý trường hợp API trả về lỗi hoặc danh sách rỗng
		    if (locations == null || locations.isEmpty()) {
		        // Xử lý lỗi hoặc trả về danh sách rỗng
		        locations = new ArrayList<>(); 
		    }
		    List<Job> jobList = new ArrayList<>();
		    jobList.add(new Job(1L, "Java Developer", locations.get(0), "IT"));
		    jobList.add(new Job(2L, "Marketing Specialist", locations.get(1), "Marketing"));
		    jobList.add(new Job(3L, "Financial Analyst", locations.get(0), "Finance"));
		    jobList.add(new Job(4L, "Registered Nurse", locations.get(1), "Healthcare"));
		    jobList.add(new Job(5L, "Civil Engineer", locations.get(0), "Construction"));
		    jobList.add(new Job(6L, "Software Engineer", locations.get(1), "IT"));
		    jobList.add(new Job(7L, "Data Scientist", locations.get(0), "IT"));
		    jobList.add(new Job(8L, "Project Manager", locations.get(1), "Construction"));
		    jobList.add(new Job(9L, "Marketing Manager", locations.get(0), "Marketing"));
		    jobList.add(new Job(10L, "Accountant", locations.get(1), "Finance"));
		    jobList.add(new Job(11L, "Teacher", locations.get(0), "Education"));
		    jobList.add(new Job(12L, "Physician", locations.get(1), "Healthcare"));
		    jobList.add(new Job(13L, "Web Developer", locations.get(0), "IT"));
		    jobList.add(new Job(14L, "Marketing Coordinator", locations.get(1), "Marketing"));
		    jobList.add(new Job(15L, "Financial Advisor", locations.get(0), "Finance"));
		    jobList.add(new Job(16L, "Pharmacist", locations.get(1), "Healthcare"));
		    jobList.add(new Job(17L, "Construction Manager", locations.get(0), "Construction"));
		    jobList.add(new Job(18L, "Software Tester", locations.get(1), "IT"));
		    jobList.add(new Job(19L, "UX Designer", locations.get(0), "IT"));
		    jobList.add(new Job(20L, "Data Analyst", locations.get(1), "IT"));
		    return jobList;
	 }
	 private List<Location> fetchLocationsFromAPI() {
		    try {
		        // 1. Tạo RestTemplate
		        RestTemplate restTemplate = new RestTemplate();
		        String apiUrl = "https://provinces.open-api.vn/api/?depth=1"; 
		        // 3. Gọi API
		        ResponseEntity<Location[]> response = restTemplate.exchange(
		            apiUrl, 
		            HttpMethod.GET, 
		            null, 
		            Location[].class
		        );

		        // 4. Kiểm tra response và trả về danh sách Location
		        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
		            return Arrays.asList(response.getBody());
		        } else {
		            // Xử lý lỗi, ví dụ: log lỗi và trả về danh sách rỗng
		            System.err.println("Lỗi khi gọi API: " + response.getStatusCode());
		            return new ArrayList<>(); 
		        }
		    } catch (Exception e) {
		        // Xử lý ngoại lệ, ví dụ: log lỗi và trả về danh sách rỗng
		        System.err.println("Lỗi khi gọi API: " + e.getMessage());
		        return new ArrayList<>();
		    }
		}
}
	
