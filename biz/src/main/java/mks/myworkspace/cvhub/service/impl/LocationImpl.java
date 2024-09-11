package mks.myworkspace.cvhub.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.repository.JobRoleRepository;
import mks.myworkspace.cvhub.repository.LocationRepository;
import mks.myworkspace.cvhub.service.LocationService;
import lombok.Getter;
@Service
public class LocationImpl implements LocationService {
	@Getter
	@Autowired
	LocationRepository repo;
	@Override
	public List<Location> fetchLocationsFromAPI() {
	    try {
	        // 1. Tạo RestTemplate
	        RestTemplate restTemplate = new RestTemplate();
	        String apiUrl = "https://provinces.open-api.vn/api/?depth=1"; 
	        // 3. Gọi API
	        ResponseEntity<Location[]> response = restTemplate.exchange(
	            apiUrl, 
	            HttpMethod.GET, 
	            null, 
	            Location[].class
	        );

	        // 4. Kiểm tra response và trả về danh sách Location
	        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
	            return Arrays.asList(response.getBody());
	        } else {
	            // Xử lý lỗi, ví dụ: log lỗi và trả về danh sách rỗng
	            System.err.println("Lỗi khi gọi API: " + response.getStatusCode());
	            return new ArrayList<>(); 
	        }
	    } catch (Exception e) {
	        // Xử lý ngoại lệ, ví dụ: log lỗi và trả về danh sách rỗng
	        System.err.println("Lỗi khi gọi API: " + e.getMessage());
	        return new ArrayList<>();
	    }
	}

	}
	
