package mks.myworkspace.cvhub.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.repository.JobRequestRepository;

public interface JobRequestService {
	JobRequestRepository getRepo();
	JobRequest createJobRequest(String title, int locationCode, Long jobRoleId, Integer experience,
			Integer salary,Long organizationId,String jobDescription,String requirementsCandidate,String benefitCandidate,LocalDate deadlineApplication);
	JobRequest updateJobRequest( JobRequest jobRequest,String title, int locationCode, Long jobRoleId,
			Integer experience, Integer salary, String jobDescription, String requirementsCandidate,
			String benefitCandidate, LocalDate deadlineApplication);
	void deleteJobRequest(JobRequest jobRequest);
}