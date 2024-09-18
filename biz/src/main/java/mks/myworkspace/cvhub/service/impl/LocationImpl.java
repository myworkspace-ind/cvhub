package mks.myworkspace.cvhub.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.repository.LocationRepository;
import mks.myworkspace.cvhub.service.LocationService;
@Service
public class LocationImpl implements LocationService {
	@Getter
	@Autowired
	LocationRepository repo;
	public final Logger logger = LoggerFactory.getLogger(this.getClass());;
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
	            
	        	logger.warn("Lỗi khi gọi API: " + response.getStatusCode());
	            return new ArrayList<>(); 
	        }
	    } catch (Exception e) {
	        
	    	logger.warn("Lỗi khi gọi API: " + e.getMessage());
	        return new ArrayList<>();
	    }
	}

	}
	
