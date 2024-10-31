package mks.myworkspace.cvhub.controller;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.cvhub.controller.model.CvDTO;
import mks.myworkspace.cvhub.entity.CV;
import mks.myworkspace.cvhub.entity.JobRole;
import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.service.CvService;
import mks.myworkspace.cvhub.service.JobRoleService;
import mks.myworkspace.cvhub.service.LocationService;
import mks.myworkspace.cvhub.service.ParsingCVService;

@Controller
public class ResumeController  extends BaseController  {
	public final Logger logger = LoggerFactory.getLogger(this.getClass());;
	@Autowired
	ParsingCVService parsingCVService;
	@Autowired
	CvService cvService;
	@Autowired
	JobRoleService jobRoleService;
	@Autowired
	LocationService locationService;

	@RequestMapping(value = { "uploadCV" }, method = RequestMethod.GET)
	public ModelAndView returnUploadCV() {
		ModelAndView mav = new ModelAndView("uploadCV/uploadCV");
		return mav;
	}

	@RequestMapping(value = { "completeCV" }, method = RequestMethod.POST)
	public ModelAndView  handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
		 ModelAndView modelAndView = new ModelAndView("uploadCV/completeCV");
		    try {
		        String content = parsingCVService.extractTextFromPdfOrWord(file);
		        Map<String, String> parsedInfo = parsingCVService.parseContent(content);
		        modelAndView.addObject("cvData", parsedInfo);
		    } catch (IOException e) {
		        modelAndView.addObject("error", "Không thể xử lý file: " + e.getMessage());
		    }
		    List<JobRole> jobRoles = jobRoleService.getRepo().findAll();
		    modelAndView.addObject("jobRoles", jobRoles);
			List<Location> locations = locationService.getRepo().findAll();
			modelAndView.addObject("locations", locations);
		    return modelAndView;
	}
	@RequestMapping(value = { "saveCV" }, method = RequestMethod.POST)
	public ModelAndView saveCV(@ModelAttribute CvDTO cvDTO) {
		ModelAndView mav = new ModelAndView("uploadCV/renderCV");
		CV cvNew =cvService.saveCV(
	            cvDTO.getFullName(),
	            cvDTO.getLocationCode(),
	            cvDTO.getJobRoleId(),
	            cvDTO.getEmail(),
	            cvDTO.getPhone(),
	            cvDTO.getEducation(),
	            cvDTO.getSkills(),
	            cvDTO.getExperience(),
	            cvDTO.getProjects(),
	            cvDTO.getCertifications(),
	            cvDTO.getActivities(),
	            cvDTO.getLogoFile()
	        );
		CV savedCV=cvService.getRepo().save(cvNew);
		  mav.addObject("cv", savedCV);
		return mav;
	}
	@GetMapping(value = "/cv/images/{logoId}")
	@ResponseBody
	public ResponseEntity<byte[]> getCVImage(@PathVariable("logoId") UUID logoId) {
	    byte[] image = cvService.getRepo().getImageByLogoId(logoId);

	    if (image == null || image.length == 0) {
	        logger.warn("CV image not found or empty for logoId: " + logoId);
	        return ResponseEntity.notFound().build();
	    }

	    return ResponseEntity.ok()
	            .contentType(MediaType.IMAGE_JPEG)
	            .body(image);
	}
}