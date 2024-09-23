package mks.myworkspace.cvhub.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import mks.myworkspace.cvhub.repository.JobRequestRepository;
import mks.myworkspace.cvhub.service.JobRequestService;

@Service
public class JobRequestImpl implements JobRequestService {

	@Getter
	@Autowired
	JobRequestRepository repo;

}
