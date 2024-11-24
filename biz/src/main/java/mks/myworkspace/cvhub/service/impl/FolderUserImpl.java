package mks.myworkspace.cvhub.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;


import mks.myworkspace.cvhub.service.FolderUserService;

@Service
public class FolderUserImpl  implements FolderUserService {

	  @Autowired
	    private ResourceLoader resourceLoader;
	  
	 

	    @Override
	    public List<String> getUserFolders() {  	  
	    	Resource resource = resourceLoader.getResource("resources/userCV");
	    	 List<String> subFolders = new ArrayList<>();
	         try {
	             File rootFolder = resource.getFile(); // Lấy đối tượng File
	             if (rootFolder.exists() && rootFolder.isDirectory()) {
	                 // Lấy danh sách thư mục con
	                 File[] directories = rootFolder.listFiles(File::isDirectory);

	                 if (directories != null) {
	                     for (File dir : directories) {
	                         subFolders.add(dir.getName()); // Lấy tên từng thư mục con
	                     }
	                 }
	             } else {
	                 System.out.println("Thư mục không tồn tại hoặc không phải là thư mục.");
	             }

	         } catch (IOException e) {
	             System.out.println("Không tìm thấy tệp: " + e.getMessage());
	         }

	         return subFolders;

	    }

	    @Override
	    public List<String> getFilesByUser(String username) {
	        // Đường dẫn tới thư mục của username
	        String userFolderPath = "resources/userCV/" + username;

	        // Lấy tài nguyên
	        Resource resource = resourceLoader.getResource(userFolderPath);

	        List<String> fileNames = new ArrayList<>();
	        try {
	            // Lấy danh sách file trong thư mục
	            File userFolder = resource.getFile();
	            System.out.print(userFolder);
	            if (userFolder.exists() && userFolder.isDirectory()) {
	                fileNames = Arrays.asList(userFolder.list());
	            }
	        } catch (IOException e) {
	            System.out.println("Không tìm thấy tệp hoặc thư mục: " + e.getMessage());
	        }

	        return fileNames;
	    }
	    
 
}
