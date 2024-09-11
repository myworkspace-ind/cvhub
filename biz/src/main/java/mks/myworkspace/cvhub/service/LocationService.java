package mks.myworkspace.cvhub.service;

import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.repository.JobRoleRepository;
import mks.myworkspace.cvhub.repository.LocationRepository;

import java.util.List;

import org.springframework.stereotype.Service;


@Service
public interface LocationService {
	LocationRepository getRepo();
	List<Location>  fetchLocationsFromAPI();
}