package mks.myworkspace.cvhub.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import lombok.Getter;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.repository.JobRequestRepository;
import mks.myworkspace.cvhub.service.JobRequestService;

@Service
public class JobRequestImpl implements JobRequestService {

	@Getter
	@Autowired
	JobRequestRepository repo;

}
