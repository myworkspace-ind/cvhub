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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.cvhub.entity.JobRequest;
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
     * @return 
	 */
	@RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
	public ModelAndView displayHome(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("home");

		initSession(request, httpSession);
		
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());
		List<Location> locations = locationService.getRepo().findAll();
        mav.addObject("locations", locations);
		return mav;
	}
	@RequestMapping(value = {"/add"}, method = RequestMethod.GET)
    public ModelAndView addJobRoles() {
		 ModelAndView mav = new ModelAndView("searchResult");
		List<Location> locations=locationService.getRepo().findAll();
		List<JobRequest> jobRequests = Arrays.asList(
            new JobRequest(1L,"Java Developer", locations.get(0), "IT",1,100),
            new JobRequest(2L,"Marketing Specialist",locations.get(0), "Marketing",2,200),
            new JobRequest(3L,"Financial Analyst", locations.get(0), "Finance",3,300),
            new JobRequest(4L,"Registered Nurse", locations.get(0), "Healthcare",4,100),
            new JobRequest(5L,"Civil Engineer", locations.get(0), "Construction",5,200),
            new JobRequest(6L,"Software Engineer", locations.get(0), "IT",6,300),
            new JobRequest(7L,"Data Scientist", locations.get(0), "IT",7,100),
            new JobRequest(8L,"Project Manager",locations.get(0), "Construction",8,200),
            new JobRequest(9L,"Marketing Manager",locations.get(0), "Marketing",1,300),
            new JobRequest(10L,"Accountant", locations.get(0), "Finance",2,100),
            new JobRequest(11L,"Teacher", locations.get(0), "Education",3,200),
            new JobRequest(12L,"Physician", locations.get(0), "Healthcare",4,300),
            new JobRequest(13L,"Java Dev", locations.get(0), "IT",4,100)
        );
        jobRoleService.getRepo().saveAll(jobRequests);

        return mav;
    }
	@RequestMapping(value = {"/addLocation"}, method = RequestMethod.GET)
    public ModelAndView addLocation() {
		 ModelAndView mav = new ModelAndView("searchResult");
		List<Location> locations = locationService.fetchLocationsFromAPI();
        
        locationService.getRepo().saveAll(locations);

        return mav;
    }
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView searchJobs( /// 
	    @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
	    @RequestParam(value = "location", required = false, defaultValue = "0") int locationCode,
	    @RequestParam(value = "industry", required = false, defaultValue = "") String industry,
	    HttpServletRequest request, 
	    HttpSession httpSession
	) {  
	    ModelAndView mav = new ModelAndView("searchResult");
	    // 1. Trước tiên, lọc theo ngành
	    List<JobRequest> jobs = jobRoleService.getRepo().findByIndustry(industry);
	    // 2. Tiếp tục lọc kết quả dựa trên các tiêu chí khác
	    List<JobRequest> searchResults = jobs.stream()
	        .filter(job -> 
	            job.getTitle().toLowerCase().contains(keyword.toLowerCase()) &&
	            (locationCode == 0 || job.getLocation().getCode() == locationCode)
	        )
	        .collect(Collectors.toList());
	    System.out.println("Total jobs in industry: " + jobs.size());
	    System.out.println("Filtered search results: " + searchResults.size());
	    searchResults.forEach(job -> System.out.println("Job: " + job.getTitle() + ", Location: " + job.getLocation().getCode()));
	    List<Location> locations = locationService.getRepo().findAll();
        mav.addObject("locations", locations);
	    mav.addObject("searchResults", searchResults);
	    mav.addObject("keyword", keyword);
	    mav.addObject("location", locationCode);
	    mav.addObject("industry", industry); 
	    try {
            // Download image from URL
            URL url = new URL("https://static.vecteezy.com/system/resources/previews/014/018/563/non_2x/amazon-logo-on-transparent-background-free-vector.jpg");
            try (InputStream inputStream = url.openStream()) {
                byte[] imageBytes = IOUtils.toByteArray(inputStream);
                
                // Convert image to Base64
                String image_base64 = Base64.getEncoder().encodeToString(imageBytes);
                
                // Add Base64 image to model
                mav.addObject("image", image_base64);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error (e.g., add error message to model)
            mav.addObject("error", "Failed to download and process the image");
        }
	    return mav;
	}
	@RequestMapping(value = {"uploadCV"}, method = RequestMethod.GET)
	public ModelAndView returnUploadCV() {
		 ModelAndView mav = new ModelAndView("uploadCV/uploadCV");
		 return mav;
	}
	@RequestMapping(value = {"completeCV"}, method = RequestMethod.POST)
	public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        String content = "";

        try {
            if (file.getContentType().equals("application/pdf")) {
                content = extractTextFromPDF(file);
            } else if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
                       file.getContentType().equals("application/msword")) {
                content = extractTextFromWord(file);
            }

            // Trích xuất thông tin cần thiết
            System.out.println("Total " + content);

        } catch (IOException e) {
            e.printStackTrace();
            // Xử lý lỗi
        }

        return "uploadResult"; // Trả về tên trang kết quả
    }
	private String extractTextFromPDF(MultipartFile file) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream());
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        document.close();
        return text;
    }

    private String extractTextFromWord(MultipartFile file) throws IOException {
        XWPFDocument document = new XWPFDocument(file.getInputStream());
        XWPFWordExtractor extractor = new XWPFWordExtractor(document);
        String text = extractor.getText();
        document.close();
        return text;
    }
}
	
