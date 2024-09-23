package mks.myworkspace.cvhub.service;

import org.springframework.stereotype.Service;

import mks.myworkspace.cvhub.repository.JobRequestRepository;
@Service
public interface JobRequestService {
	JobRequestRepository getRepo();
}
