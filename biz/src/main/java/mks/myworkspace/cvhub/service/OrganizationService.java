package mks.myworkspace.cvhub.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.repository.OrganizationRepository;

@Service
public interface OrganizationService {
	OrganizationRepository getRepo();

	Organization createOrganization(String title, MultipartFile logoFile, String website, String summary, String detail,
			String location);

	byte[] downloadImage(MultipartFile logoFile) throws IOException;

	boolean isOwner(Long organizationId, String userEmail);

	Organization findByUserId(Long id);

	List<Organization> searchByTitle(String title); // Th�m ch?c nang t�m ki?m c�ng ty b?ng t�n c�ng ty : ngu?i th�m
													// LeDaoNhanSam

	List<Organization> findByTitleContaining(String title);
}
