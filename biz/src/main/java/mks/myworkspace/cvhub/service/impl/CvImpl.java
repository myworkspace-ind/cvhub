package mks.myworkspace.cvhub.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import mks.myworkspace.cvhub.entity.CV;
import mks.myworkspace.cvhub.entity.JobRole;
import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.repository.CvRepository;
import mks.myworkspace.cvhub.repository.OrganizationRepository;
import mks.myworkspace.cvhub.service.CvService;
import mks.myworkspace.cvhub.service.JobRequestService;
import mks.myworkspace.cvhub.service.JobRoleService;
import mks.myworkspace.cvhub.service.LocationService;

@Service
public class CvImpl implements CvService {

	@Getter
	@Autowired
	CvRepository repo;
	@Autowired
	LocationService locationService;
	@Autowired
	JobRoleService jobRoleService;

	@Override
	public CV saveCV(String fullName, int locationCode, Long jobRoleId, String email, String phone, String education,
			String skills, String experience, String projects, String certifications, String activities,
			MultipartFile logoFile, User user) {
		try {
			byte[] logo = downloadImage(logoFile);
			UUID logoID = UUID.randomUUID(); // Tạo một UUID ngẫu nhiên
			Location location = locationService.getRepo().findById(locationCode)
					.orElseThrow(() -> new IllegalArgumentException("Invalid location code"));

			JobRole jobRole = jobRoleService.getRepo().findById(jobRoleId).orElse(null); // Có thể là null nếu không tìm
																							// thấy
			CV cv = new CV(fullName, jobRole, email, phone, location, education, skills, experience, projects,
					certifications, activities, logoID, logo);

			// Set the user
			cv.setUser(user);

			return cv;
		} catch (IOException e) {
			e.printStackTrace();
			// Create CV without logo but with user in case of logo processing error
			CV cv = new CV(fullName, null, email, phone, null, education, skills, experience, projects, certifications,
					activities, null, null);
			cv.setUser(user);
			return cv;
		}

	}

	public byte[] downloadImage(MultipartFile logoFile) throws IOException {
		if (logoFile != null && !logoFile.isEmpty()) {
			return logoFile.getBytes();
		}
		throw new IOException("Logo file is null or empty");
	}

	@Override
	public long getSelectedCVCount(Long userId) {

		 return repo.getSelectedCVCount(userId);
	}



	@Override
	public List<CV> findCVsByUserId(Long id) {
		return repo.findCVsByUserId(id);
	}

	@Override
	public void setPrimaryCV(Long id, Long id2) {
		List<CV> userCVs = repo.findByUserId(id2);
        userCVs.forEach(cv -> cv.setIsprimary(false));
        repo.saveAll(userCVs);
        
        // Đặt CV được chọn thành primary
        CV selectedCV = repo.findById(id).orElse(null);
        if (selectedCV != null && selectedCV.getUser().getId().equals(id2)) {
            selectedCV.setIsprimary(true);
            repo.save(selectedCV);
        }
	}

	// Phương thức để lấy danh sách CV phân trang
    public Page<CV> getPaginatedCVs(Pageable pageable) {
        return repo.findAll(pageable);
    }

    // Phương thức tìm kiếm và phân trang
    public Page<CV> searchCVs(String keyword, Pageable pageable) {
        return repo.search(keyword, pageable);
    }
	
}