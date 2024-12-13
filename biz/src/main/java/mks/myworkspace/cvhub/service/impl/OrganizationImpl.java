package mks.myworkspace.cvhub.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.repository.OrganizationRepository;
import mks.myworkspace.cvhub.service.OrganizationService;

@Service
public class OrganizationImpl implements OrganizationService {

	@Getter
	@Autowired
	OrganizationRepository repo;

	@Override
	public Organization createOrganization(String title, MultipartFile logoFile, String website, String summary, String detail, String location) {
	    try {
	        byte[] logo = downloadImage(logoFile);
	        UUID logoID = UUID.randomUUID(); // Tạo một UUID ngẫu nhiên
	        return new Organization(title, logoID, logo, website, summary, detail, location);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return new Organization(title, null, null, website, summary, detail, location); // Trả về tổ chức mà không có logo nếu xảy ra lỗi
	    }
	}

	@Override
	public byte[] downloadImage(MultipartFile logoFile) throws IOException {
	    if (logoFile != null && !logoFile.isEmpty()) {
	        return logoFile.getBytes();
	    }
	    throw new IOException("Logo file is null or empty");
	}

	@Override
	public boolean isOwner(Long organizationId, String userEmail) {
		        Organization org = repo.findById(organizationId).orElse(null);
		        if (org == null || org.getUser() == null) {
		            return false;
		        }
		        return org.getUser().getEmail().equals(userEmail);
	}

	@Override
	public Organization findByUserId(Long id) {
		return repo.findByUserId(id);
	}

	@Override
	public List<Organization> searchByTitle(String title) {

		return repo.searchByTitle(title); // thêm bới ledaonhansam
	}
	
	@Override
	public List<Organization> findByTitleContaining(String title) {
		return repo.findByTitleContaining(title);
	}

	@Override
	public Long getTotalJobRequestsByOrganizationId(Long organizationId) {
		return repo.countByOrganizationId(organizationId);
	}

	@Override
	public List<Organization> getSortedOrganizations(String sort) {
		switch (sort) {
        case "created_date_asc":
            return repo.findAllByOrderByCreatedDateAsc();
        case "created_date_desc":
            return repo.findAllByOrderByCreatedDateDesc();
        case "job_count_asc":
            return repo.findAllByOrderByJobCountAsc();
        case "job_count_desc":
            return repo.findAllByOrderByJobCountDesc();
        default:
            return repo.findAll(); 
    }	  

	}
}
