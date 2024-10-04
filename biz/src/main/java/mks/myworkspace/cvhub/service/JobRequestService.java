package mks.myworkspace.cvhub.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.repository.JobRequestRepository;
@Service
public interface JobRequestService {
	JobRequestRepository getRepo();
	JobRequest createJobRequest(String title, int locationCode,int districtCode, Long jobRoleId, Integer experience,
			Integer salary,Long organizationId,String jobDescription);
}