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
import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.cvhub.controller.model.JobSearchDTO;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.JobRole;
import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.service.JobRoleService;
import mks.myworkspace.cvhub.service.LocationService;
import mks.myworkspace.cvhub.service.OrganizationService;
import mks.myworkspace.cvhub.service.SearchJobService;

/**
 * Handles requests for the application home page.
 */
@Controller

public class HomeController extends BaseController {
	@Autowired
	OrganizationService organizationService;
	@Autowired
	JobRoleService jobRoleService;
	@Autowired
	LocationService locationService;
	@Autowired
	SearchJobService searchjobService;
	public final Logger logger = LoggerFactory.getLogger(this.getClass());;

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
		List<Location> locations = locationService.getRepo().findAll();
		mav.addObject("locations", locations);
		return mav;
	}

	@GetMapping("/job-roles")
	@ResponseBody
	public List<JobRole> getAllJobRoles() {
		return jobRoleService.getRepo().findAll();
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView searchJobs(@ModelAttribute JobSearchDTO jobSearchDTO, HttpServletRequest request,
			HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("searchResult");
		List<JobRequest> searchResults = searchjobService.searchJobRequest(jobSearchDTO.getKeyword(),
				jobSearchDTO.getLocation(), jobSearchDTO.getIndustry());
		mav.addObject("searchResults", searchResults);

		return mav;
	}

	@GetMapping(value = "/images/{logoId}")
	@ResponseBody
	public ResponseEntity<byte[]> getImage(@PathVariable("logoId") UUID logoId) {
		byte[] image = organizationService.getRepo().getImageByLogoId(logoId);

		if (image == null || image.length == 0) {
			logger.warn("Image not found or empty for logoId: " + logoId);
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
	}

	@RequestMapping(value = { "uploadCV" }, method = RequestMethod.GET)
	public ModelAndView returnUploadCV() {
		ModelAndView mav = new ModelAndView("uploadCV/uploadCV");
		return mav;
	}

	@RequestMapping(value = { "completeCV" }, method = RequestMethod.POST)
	public String handleFileUpload(@RequestParam("file") MultipartFile file) {
		String content = "";

		try {
			if (file.getContentType().equals("application/pdf")) {
				content = extractTextFromPDF(file);
			} else if (file.getContentType()
					.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
					|| file.getContentType().equals("application/msword")) {
				content = extractTextFromWord(file);
			}

			// Trích xuất thông tin cần thiết
			logger.info("Total " + content);

		} catch (IOException e) {
			logger.error("Đã xảy ra lỗi: {}", e.getMessage(), e);
		}

		return "uploadResult"; //
	}
	  @GetMapping("/details")
	   public ModelAndView getJobDetails(@RequestParam("id") Long jobId) {
	        // Replace with your service call to fetch the job details using jobId
			ModelAndView mav = new ModelAndView("V");
	        return mav;
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

	public byte[] uuidToBytes(UUID uuid) {
		ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[256]);
		byteBuffer.putLong(uuid.getMostSignificantBits());
		byteBuffer.putLong(uuid.getLeastSignificantBits());
		return byteBuffer.array();
	}

}
