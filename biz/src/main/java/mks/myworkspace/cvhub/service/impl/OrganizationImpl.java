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
import mks.myworkspace.cvhub.dao.OrganizationDao;
import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.model.OrganizationJDBC;
import mks.myworkspace.cvhub.repository.OrganizationRepository;
import mks.myworkspace.cvhub.service.OrganizationService;

@Service
public class OrganizationImpl implements OrganizationService {

	@Getter
	@Autowired
	OrganizationRepository repo;
	@Autowired
	private OrganizationDao organizationDao;

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
	public OrganizationJDBC createOrganizationJdbc(String title, MultipartFile logoFile, 
	        String website, String summary, String detail, String location) {
	    try {
	        // Xử lý logo file
	        byte[] logo = null;
	        UUID logoID = null;
	        
	        if (logoFile != null && !logoFile.isEmpty()) {
	            logo = logoFile.getBytes();
	            logoID = UUID.randomUUID();
	        }
	        
	        // Tạo đối tượng OrganizationJDBC
	        OrganizationJDBC organization = new OrganizationJDBC(
	            title, logoID, logo, website, summary, detail, location, null
	        );
	        
	        // Lưu vào database thông qua DAO
	        return organizationDao.save(organization);
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	        // Tạo organization không có logo nếu xảy ra lỗi
	        OrganizationJDBC organization = new OrganizationJDBC(
	            title, null, null, website, summary, detail, location, null
	        );
	        return organizationDao.save(organization);
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
	public Organization findByOrganizationId(Long organizationId) {
		return repo.findOrganizationById(organizationId);
	}
	
	@Override
	public Organization updateOrganization(Organization organization, String title, MultipartFile logoFile, String website, String summary, String detail, String location) {
	    try {
	        byte[] logo = downloadImage(logoFile);
	        UUID logoID = UUID.randomUUID();
	        organization.setTitle(title);
	        organization.setLogoID(logoID);
	        organization.setLogo(logo);
	        organization.setWebsite(website);
	        organization.setSummary(summary);
	        organization.setDetail(detail);
	        organization.setLocation(location);
	        return organization;
	    } catch (IOException e) {
	        e.printStackTrace();
	        organization.setTitle(title);
	        organization.setLogoID(null);
	        organization.setLogo(null);
	        organization.setWebsite(website);
	        organization.setSummary(summary);
	        organization.setDetail(detail);
	        organization.setLocation(location);
	        return organization;
	    }
	}
	
	@Override
	public OrganizationJDBC updateOrganizationJdbc(OrganizationJDBC organization, String title, 
	       MultipartFile logoFile, String website, String summary, String detail, String location) {
	   try {
	       // Xử lý logo file nếu có
	       byte[] logo = null;
	       UUID logoID = null;
	       if (logoFile != null && !logoFile.isEmpty()) {
	           logo = downloadImage(logoFile);
	           logoID = UUID.randomUUID();
	       }

	       // Cập nhật thông tin cho organization
	       organization.setTitle(title);
	       organization.setLogoID(logoID);
	       organization.setLogo(logo);
	       organization.setWebsite(website);
	       organization.setSummary(summary);
	       organization.setDetail(detail);
	       organization.setLocation(location);

	       organizationDao.update(organization);
	       
	       return organization;
	       
	   } catch (IOException e) {
	       e.printStackTrace();
	       organization.setTitle(title);
	       organization.setLogoID(null);
	       organization.setLogo(null);
	       organization.setWebsite(website);
	       organization.setSummary(summary);
	       organization.setDetail(detail);
	       organization.setLocation(location);
	       
	       organizationDao.update(organization);
	       return organization;
	   }
	}
	
	 public List<Organization> searchByLocation(String location) {
	        return repo.searchByLocation(location);
	    }

	    // Tìm kiếm theo tên công ty và vị trí
	public List<Organization> searchByTitleAndLocation(String companyName, String location) {
	        return repo.searchByTitleAndLocation(companyName, location);
	    }
}
