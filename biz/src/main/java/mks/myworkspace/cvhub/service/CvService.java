package mks.myworkspace.cvhub.service;

import org.springframework.web.multipart.MultipartFile;

import mks.myworkspace.cvhub.entity.CV;
import mks.myworkspace.cvhub.repository.CvRepository;

public interface CvService{
	CvRepository getRepo();
	CV saveCV( String fullName,int locationCode, Long jobRoleId,String email,String phone,String education,String skills,String experience,String projects,String certifications,String activities, MultipartFile logoFile);
}
