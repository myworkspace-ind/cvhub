package mks.myworkspace.cvhub.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.repository.OrganizationRepository;

@Service
public interface OrganizationService {
	OrganizationRepository getRepo();
	Organization createOrganization(String title, MultipartFile logoFile, String website, String summary, String detail, String location);
	byte[] downloadImage(MultipartFile logoFile) throws IOException ;
	 boolean isOwner(Long organizationId, String userEmail);
	 Organization findByUserId(Long id);
}
package mks.myworkspace.cvhub.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.repository.OrganizationRepository;

public interface OrganizationService {
	OrganizationRepository getRepo();
	Organization createOrganization(String title, MultipartFile logoFile, String website, String summary, String detail, String location);
	byte[] downloadImage(MultipartFile logoFile) throws IOException ;
}
