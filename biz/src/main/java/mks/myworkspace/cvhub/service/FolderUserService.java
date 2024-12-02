package mks.myworkspace.cvhub.service;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Service;


@Service
public interface FolderUserService {

	List<String> getFilesByUser(String username);

	List<String> getUserFolders();



	

	 
	

}
