package mks.myworkspace.cvhub.service.impl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import mks.myworkspace.cvhub.entity.CV;
import mks.myworkspace.cvhub.entity.JobRole;
import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.repository.CvRepository;
import mks.myworkspace.cvhub.repository.OrganizationRepository;
import mks.myworkspace.cvhub.service.CvService;
import mks.myworkspace.cvhub.service.JobRequestService;
import mks.myworkspace.cvhub.service.JobRoleService;
import mks.myworkspace.cvhub.service.LocationService;

@Service
public class CvImpl implements CvService{

	@Getter
	@Autowired
	CvRepository repo;
	@Autowired
	LocationService locationService;
	@Autowired
	JobRoleService jobRoleService;
	@Override
	public CV saveCV(String fullName, int locationCode, Long jobRoleId, String email, String phone, String education, String skills,
			String experience, String projects, String certifications, String activities, MultipartFile logoFile) {
		 try {
		        byte[] logo = downloadImage(logoFile);
		        UUID logoID = UUID.randomUUID(); // Tạo một UUID ngẫu nhiên
		        Location location = locationService.getRepo().findById(locationCode)
						.orElseThrow(() -> new IllegalArgumentException("Invalid location code"));

				JobRole jobRole = jobRoleService.getRepo().findById(jobRoleId).orElse(null); // Có thể là null nếu không tìm
																								// thấy
		        return new CV(fullName,jobRole,email,phone,location,education,skills,experience,projects,certifications,activities,logoID,logo);
		    } catch (IOException e) {
		        e.printStackTrace();
		        return new CV(fullName,null,email,phone,null,education,skills,experience,projects,certifications,activities,null,null); // Trả về tổ chức mà không có logo nếu xảy ra lỗi
		    }
		
	}
	public byte[] downloadImage(MultipartFile logoFile) throws IOException {
	    if (logoFile != null && !logoFile.isEmpty()) {
	        return logoFile.getBytes();
	    }
	    throw new IOException("Logo file is null or empty");
	}
}