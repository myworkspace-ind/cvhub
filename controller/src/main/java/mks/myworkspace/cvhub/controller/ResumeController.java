package mks.myworkspace.cvhub.controller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mks.myworkspace.cvhub.controller.model.CvDTO;
import mks.myworkspace.cvhub.entity.CV;
import mks.myworkspace.cvhub.entity.JobApplication;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.JobSaved;
import mks.myworkspace.cvhub.entity.FollowedOrganization;
import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.entity.JobRole;
import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.service.CvService;
import mks.myworkspace.cvhub.service.JobApplicationService;
import mks.myworkspace.cvhub.service.JobRequestService;
import mks.myworkspace.cvhub.service.JobRoleService;
import mks.myworkspace.cvhub.service.JobSavedService;
import mks.myworkspace.cvhub.service.FollowedOrganizationService;
import mks.myworkspace.cvhub.service.OrganizationService;
import mks.myworkspace.cvhub.service.LocationService;
import mks.myworkspace.cvhub.service.ParsingCVService;
import mks.myworkspace.cvhub.service.UserService;

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
	JobApplicationService jobApplicationService;
	@Autowired
	JobSavedService jobSavedService;
	@Autowired
	FollowedOrganizationService followedOrganizationService;
	@Autowired
	OrganizationService organizationService;
	@Autowired
	LocationService locationService;
	 @Autowired
	  UserService userService;
	 @Autowired
		JobRequestService jobRequestService;
	 
	 @Value("${file.storage.pathCV}") 
	 private String storagePath;
	 
	 @RequestMapping(value = { "uploadCV" }, method = RequestMethod.GET)
	 public ModelAndView returnUploadCV(HttpSession session) {
	     ModelAndView mav = new ModelAndView("uploadCV/uploadCV");	   
	     return mav;
	 }


	@RequestMapping(value = { "completeCV" }, method = RequestMethod.POST)
	public ModelAndView handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
	    ModelAndView modelAndView = new ModelAndView("uploadCV/completeCV");
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    User currentUser = userService.findUserByEmail(auth.getName());

	    String userId = currentUser.getId().toString();
	    Path targetLocation = Paths.get(storagePath + userId + "/" + file.getOriginalFilename());
	    File targetDir = targetLocation.getParent().toFile();

	    // Kiểm tra và tạo thư mục nếu không tồn tại
	    if (!targetDir.exists()) {
	        targetDir.mkdirs();
	    }

	    try {
	        // Thực hiện phân tích nội dung của file nếu có thể
	        String content = parsingCVService.extractTextFromPdfOrWord(file);
	        Map<String, String> parsedInfo = parsingCVService.parseContent(content, currentUser);
	        modelAndView.addObject("cvData", parsedInfo);

	        // Sao chép file đến vị trí mục tiêu
	        File targetFile = targetLocation.toFile();
	        file.transferTo(targetFile);

	    } catch (IOException e) {
	        modelAndView.addObject("error", "Không thể xử lý file: " + e.getMessage());
	    }

	    // Thêm dữ liệu công việc và vị trí
	    List<JobRole> jobRoles = jobRoleService.getRepo().findAll();
	    modelAndView.addObject("jobRoles", jobRoles);
	    List<Location> locations = locationService.getRepo().findAll();
	    modelAndView.addObject("locations", locations);

	    return modelAndView;
	}


	@RequestMapping(value = { "saveCV" }, method = RequestMethod.POST)
	public ModelAndView saveCV(@ModelAttribute CvDTO cvDTO) {
		ModelAndView mav = new ModelAndView("uploadCV/renderCV");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return new ModelAndView("redirect:/login");
        }
		User currentUser = userService.findUserByEmail(auth.getName());
        if (currentUser == null) {
            mav.setViewName("error");
            mav.addObject("errorMessage", "User not found");
            return mav;
        }
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
	            cvDTO.getLogoFile(),
	            currentUser
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
	@GetMapping("/profile/cv/manage")
    public ModelAndView manageCV() {
        ModelAndView mav = new ModelAndView("signInOut/manageCV");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth == null || !auth.isAuthenticated()) {
            return new ModelAndView("redirect:/login");
        }
        
        User currentUser = userService.findUserByEmail(auth.getName());
        if (currentUser == null) {
            mav.setViewName("error");
            mav.addObject("errorMessage", "User not found");
            return mav;
        }
        
        List<CV> userCVs = cvService.findCVsByUserId(currentUser.getId());
        Long selectedCount = cvService.getSelectedCVCount(currentUser.getId());
        
        String userId = currentUser.getId().toString();
        String userCVPath = storagePath + userId;

        // Lấy danh sách các tệp trong thư mục của người dùng
        File folder = new File(userCVPath);
        File[] listOfFiles = folder.listFiles();
        
        List<String> fileNames = new ArrayList<>();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    fileNames.add(file.getName());
                }
            }
        }

        String userDefaultCVPath = storagePath + userId + "/main"; // Thư mục main của người dùng

        // Lấy danh sách các tệp trong thư mục main
        File folderMain = new File(userDefaultCVPath);
        File[] listOfDefaultFile = folderMain.listFiles();

        String defaultFileName = null; // Tên tệp mặc định
        if (listOfDefaultFile != null && listOfDefaultFile.length == 1) {
            File file = listOfDefaultFile[0];
            if (file.isFile()) {
                defaultFileName = file.getName();
            }
        } else if (listOfDefaultFile == null || listOfDefaultFile.length == 0) {
            mav.addObject("message", "Không tìm thấy tệp trong thư mục main.");
        } else {
            mav.addObject("message", "Thư mục main chứa nhiều hơn một tệp. Vui lòng kiểm tra.");
        }

        mav.addObject("defaultFileName", defaultFileName);
        mav.addObject("fileNames", fileNames);
        mav.addObject("user", currentUser);
        mav.addObject("cvList", userCVs);
        mav.addObject("selectedCvCount", selectedCount);
        
        return mav;
    }
    
	@PostMapping("/cv/{id}/setPrimary")
	public ModelAndView setPrimaryCV(@PathVariable Long id) {
	    ModelAndView mav = new ModelAndView();
	    try {
	        // Lấy thông tin user hiện tại
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        User currentUser = userService.findUserByEmail(auth.getName());

	        // Tìm và kiểm tra CV
	        CV cv = cvService.getRepo().findById(id).orElse(null);
	        if (cv != null && cv.getUser().getId().equals(currentUser.getId())) {
	            // Set primary CV
	            cvService.setPrimaryCV(id, currentUser.getId());
	            
	            // Load lại dữ liệu cho view
	            mav.addObject("user", currentUser);
	            mav.addObject("selectedCvCount", cvService.getSelectedCVCount(currentUser.getId()));
	            // Thêm thông báo thành công
	            mav.addObject("success", "Đã đặt làm CV chính thành công");
	            
	            // Nếu bạn cần load thêm dữ liệu khác cho trang manageCV
	            // Ví dụ: danh sách CV
	            mav.addObject("cvList", cvService.getRepo().findByUserId(currentUser.getId()));
	            
	        } else {
	            // Xử lý khi không tìm thấy CV hoặc không có quyền
	            mav.addObject("error", "Không thể đặt làm CV chính. Vui lòng thử lại");
	        }
	    } catch (Exception e) {
	        mav.addObject("error", "Có lỗi xảy ra. Vui lòng thử lại sau");
	    }
	    
	    mav.setViewName("signInOut/manageCV");
	    return mav;
	}
	
    @GetMapping("/renderCV/{id}")
    public ModelAndView renderExistingCV(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("uploadCV/renderCV");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth == null || !auth.isAuthenticated()) {
            return new ModelAndView("redirect:/login");
        }
        
        User currentUser = userService.findUserByEmail(auth.getName());
        CV cv = cvService.getRepo().findById(id).orElse(null);
        
        if (cv == null || !cv.getUser().getId().equals(currentUser.getId())) {
            mav.setViewName("error");
            mav.addObject("errorMessage", "CV không tồn tại hoặc bạn không có quyền truy cập");
            return mav;
        }
        
        mav.addObject("cv", cv);
        return mav;
    }
	@GetMapping("/renderCVPrimary/{id}")
    public ModelAndView renderPrimaryCV(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("uploadCV/renderCV");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth == null || !auth.isAuthenticated()) {
            return new ModelAndView("redirect:/login");
        }
        
        User currentUser = userService.findUserByEmail(auth.getName());
        CV cv = cvService.getRepo().findPrimaryByUserId(id);
        
        if (cv == null) {
            mav.setViewName("error");
            mav.addObject("errorMessage", "CV không tồn tại hoặc bạn không có quyền truy cập");
            return mav;
        }
        
        mav.addObject("cv", cv);
        return mav;
    }
	
	@RequestMapping(value = { "/profile/applications","/profile/application" }, method = RequestMethod.GET)
	public ModelAndView viewAppliedJobs(HttpServletRequest request) {
	    ModelAndView mav ;
	    String requestURI = request.getRequestURI();
		String viewName = "signInOut/appliedJobs";
		if (requestURI.contains("/profile/application")) {
			viewName = "signInOut/appliedJob";  // Nếu là /searchJob, trả về view searchJob
		}
		mav = new ModelAndView(viewName);
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    User currentUser = userService.findUserByEmail(auth.getName());
	    // Lấy danh sách các JobApplication của người dùng
	    List<JobApplication> applications = jobApplicationService.getApplicationsByUser(currentUser);
	  
	    // Thêm danh sách vào model
	    mav.addObject("applications", applications);
	    return mav;
	}
	
	@PostMapping("/applyJob/{jobRequestId}")
	public ModelAndView applyForJob(@PathVariable Long jobRequestId,RedirectAttributes redirectAttributes) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
			return new ModelAndView("redirect:/login");
		}
	    User currentUser = userService.findUserByEmail(auth.getName());
	    ModelAndView mav = new ModelAndView("redirect:/profile/application");
	    // Kiểm tra xem người dùng đã ứng tuyển chưa
	    boolean hasApplied = jobApplicationService.hasUserApplied(currentUser, jobRequestId);
	    if (hasApplied) {

	    	 redirectAttributes.addFlashAttribute("errorMessage", "Bạn đã ứng tuyển vào công việc này.");
	        return mav;
	    }
	    
	    JobRequest jobRequest = jobRequestService.getRepo().findById(jobRequestId).orElse(null);
	    if (jobRequest != null) {
	        jobApplicationService.AddJobApplication(currentUser, jobRequest);
	    }
	    
	    return mav;
	}
	
	@PostMapping("/profile/applications/delete/{id}")
	public ModelAndView deleteApplication(@PathVariable Long id, RedirectAttributes redirectAttributes) {
	    ModelAndView mav = new ModelAndView("redirect:/profile/applications");
	    try {
	        jobApplicationService.deleteApplicationById(id);
	        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa đơn ứng tuyển thành công.");
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi xóa đơn ứng tuyển: " + e.getMessage());
	    }
	    return mav;
	}
	
	@PostMapping("/profile/application/delete/{id}")
	public ModelAndView deleteApplication1(@PathVariable Long id, RedirectAttributes redirectAttributes) {
	    ModelAndView mav = new ModelAndView("redirect:/profile/application");
	    try {
	        jobApplicationService.deleteApplicationById(id);
	        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa đơn ứng tuyển thành công.");
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi xóa đơn ứng tuyển: " + e.getMessage());
	    }
	    return mav;
	}
	@RequestMapping(value = { "/profile/savedjob" }, method = RequestMethod.GET)
	public ModelAndView viewSavedJobs() {
	    ModelAndView mav = new ModelAndView("signInOut/savedJob");
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    User currentUser = userService.findUserByEmail(auth.getName());

	    // Lấy danh sách các JobApplication của người dùng
	    List<JobSaved> savedJobs = jobSavedService.getJobSavedByUser(currentUser);
	  
	    // Thêm danh sách vào model
	    mav.addObject("savedJobs", savedJobs);
	    return mav;
	}
	@RequestMapping(value = { "/profile/followedorganization" }, method = RequestMethod.GET)
	public ModelAndView viewFollowedOrganizations() {
	    ModelAndView mav = new ModelAndView("signInOut/followedOrganization");
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    User currentUser = userService.findUserByEmail(auth.getName());

	    // Lấy danh sách các JobApplication của người dùng
	    List<FollowedOrganization> follow = followedOrganizationService.getFollowedOrganizationByUser(currentUser);
	  
	    // Thêm danh sách vào model
	    mav.addObject("followedOrganization", follow);
	    return mav;
	}
	@PostMapping("/saveJob/{jobRequestId}")
	public ModelAndView savedJob(@PathVariable Long jobRequestId, RedirectAttributes redirectAttributes) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    User currentUser = userService.findUserByEmail(auth.getName());
	    ModelAndView mav = new ModelAndView("redirect:/jobrequests/" + jobRequestId);

	    // Kiểm tra xem người dùng đã lưu công việc chưa
	    boolean hasSaved = jobSavedService.hasJobSaved(currentUser, jobRequestId);
	    if (hasSaved) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Bạn đã lưu công việc này.");
	        return mav;
	    }

	    JobRequest jobRequest = jobRequestService.getRepo().findById(jobRequestId).orElse(null);
	    if (jobRequest != null) {
	        jobSavedService.AddJobSaved(currentUser, jobRequest);
	        redirectAttributes.addFlashAttribute("successMessage", "Đã lưu công việc thành công.");
	    } else {
	        redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy công việc.");
	    }

	    return mav;
	}
	@PostMapping("/organization/followedorganization/{orgId}")
	public ModelAndView followedOrganization(@PathVariable Long orgId, RedirectAttributes redirectAttributes) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    User currentUser = userService.findUserByEmail(auth.getName());
	    ModelAndView mav = new ModelAndView("redirect:/organization/" + orgId);

	    // Kiểm tra xem người dùng đã lưu công việc chưa
	    boolean hasSaved = followedOrganizationService.hasFollowedOrganization(currentUser, orgId);
	    if (hasSaved) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Bạn đã theo dõi công ty này.");
	        return mav;
	    }

	    Organization organization = organizationService.getRepo().findById(orgId).orElse(null);
	    if (organization != null) {
	    	followedOrganizationService.AddFollowedOrganization(currentUser, organization);
	        redirectAttributes.addFlashAttribute("successMessage", "Đã theo dõi công ty thành công.");
	    } else {
	        redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy công ty này.");
	    }

	    return mav;
	}
	@PostMapping("/profile/savedjob/delete/{id}")
	public ModelAndView deleteJobSaved(@PathVariable Long id, RedirectAttributes redirectAttributes) {
	    ModelAndView mav = new ModelAndView("redirect:/profile/savedjob");
	    try {
	    	jobSavedService.deleteJobSavedById(id);
	        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa công việc thành công.");
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi xóa công việc: " + e.getMessage());
	    }
	    return mav;
	}
	@PostMapping("/profile/followedorganization/delete/{id}")
	public ModelAndView deleteFollowedOrganization(@PathVariable Long id, RedirectAttributes redirectAttributes) {
	    ModelAndView mav = new ModelAndView("redirect:/profile/followedorganization");
	    try {
	    	followedOrganizationService.deleteFollowedOrganizationById(id);
	        redirectAttributes.addFlashAttribute("successMessage", "Đã bỏ theo dõi thành công.");
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi bỏ theo dõi: " + e.getMessage());
	    }
	    return mav;
	}
	@PostMapping("/setDefaultCV/{fileName}")
    public ModelAndView setDefaultCV(@PathVariable("fileName") String fileName, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth == null || !auth.isAuthenticated()) {
            return new ModelAndView("redirect:/login");
        }
        
        User currentUser = userService.findUserByEmail(auth.getName());
        if (currentUser == null) {
        	return new ModelAndView("error", "errorMessage", "User not found");
        }
        String userId = currentUser.getId().toString(); // Thay bằng cách lấy ID thực tế của người dùng

        // Định nghĩa đường dẫn thư mục lưu trữ CV và thư mục main
        String userCVPath = storagePath + userId;
        File sourceFile = new File(userCVPath + "/" + fileName);
        File targetDir = new File(userCVPath + "/main");

        // Kiểm tra nếu tệp tồn tại và di chuyển tệp vào thư mục main
        if (sourceFile.exists()) {
            try {
                // Xóa tất cả tệp trong thư mục main trước khi sao chép
                if (targetDir.exists() && targetDir.isDirectory()) {
                    for (File file : targetDir.listFiles()) {
                        if (!file.delete()) {
                            throw new IOException("Không thể xóa tệp: " + file.getName());
                        }
                    }
                } else {
                    // Tạo thư mục main nếu chưa tồn tại
                    targetDir.mkdirs();
                }

                // Sao chép tệp vào thư mục main
                File targetFile = new File(targetDir, fileName);
                Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                redirectAttributes.addFlashAttribute("message", "Tệp CV đã được đặt làm mặc định!");
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("message", "Lỗi khi xử lý tệp CV: " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "Tệp không tồn tại.");
        }
        return new ModelAndView("redirect:/profile/cv/manage");
    }
	@RequestMapping(value = "/CV/download/{fileName}", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable("fileName") String fileName) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByEmail(auth.getName());
        String userId = currentUser.getId().toString(); // Thay bằng cách lấy ID thực tế của người dùng

	    String filePath = storagePath + userId + "/" + fileName;
	    File file = new File(filePath);
	    
	    // Kiểm tra nếu tệp không tồn tại
	    if (!file.exists()) {
	        throw new FileNotFoundException("Tệp không tồn tại");
	    }

	    // Đọc tệp vào mảng byte
	    byte[] fileContent = Files.readAllBytes(file.toPath());
	    ByteArrayResource resource = new ByteArrayResource(fileContent);
	    
	    // Thiết lập content disposition cho phép tải tệp
	    String contentDisposition = "attachment; filename=\"" + fileName + "\"";
	    
	    // Trả về ResponseEntity với các header cần thiết
	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .body(resource);
	}
	@RequestMapping(value = "/CV/delete/{fileName}", method = RequestMethod.POST)
	public ModelAndView deleteFile(@PathVariable("fileName") String fileName, RedirectAttributes redirectAttributes) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByEmail(auth.getName());
        String userId = currentUser.getId().toString();
	    String filePath = storagePath + userId + "/" + fileName;
	    File file = new File(filePath);

	    if (file.exists() && file.delete()) {
	        redirectAttributes.addFlashAttribute("message", "Tệp đã được xóa thành công!");
	    } else {
	        redirectAttributes.addFlashAttribute("error", "Không thể xóa tệp.");
	    }

	    return new ModelAndView("redirect:/profile/cv/manage");
	}
}