package mks.myworkspace.cvhub.service.impl;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;


import mks.myworkspace.cvhub.service.FolderUserService;

@Service
public class FolderUserImpl  implements FolderUserService {

	 private static final String ROOT_DIR = "src/main/webapp/resources/static/userCV";
	

	    @Override
	    public List<String> getUserFolders() {
	    	
	    	File rootFolder = new File(ROOT_DIR);

	    	if (!rootFolder.exists()) {
	    	    rootFolder.mkdirs(); // Tạo thư mục gốc nếu chưa tồn tại
	    	}

	    	// Lấy danh sách thư mục con
	    	File[] directories = rootFolder.listFiles(File::isDirectory);

//	    	if (directories == null || directories.length == 0) {
//	    	    System.out.println("Không tìm thấy thư mục con nào.");
//	    	    return Collections.emptyList();
//	    	}

	    	// Chuyển danh sách thư mục thành danh sách tên
	    	List<String> folderNames = Arrays.stream(directories)
	    	        .map(File::getName)
	    	        .collect(Collectors.toList());

	    	return folderNames;

	    }

	    @Override
	    public List<String> getFilesByUser(String username) {
	        File userFolder = new File(ROOT_DIR + "/" + username);
	        if (!userFolder.exists()) {
	            return Collections.emptyList();
	        }

	        // Lấy danh sách tệp trong thư mục
	        String[] files = userFolder.list((current, name) -> new File(current, name).isFile());
	        return files != null ? Arrays.asList(files) : Collections.emptyList();
	    }	
 
}
