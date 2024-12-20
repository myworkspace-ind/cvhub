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
import mks.myworkspace.cvhub.model.JobRequestJDBC;
@Service
public interface JobRequestService {
	JobRequestRepository getRepo();
	JobRequest createJobRequest(String title, int locationCode, Long jobRoleId, Integer experience,
			Integer salary,Long organizationId,String jobDescription,String requirementsCandidate,String benefitCandidate,LocalDate deadlineApplication);
	JobRequest updateJobRequest( JobRequest jobRequest,String title, int locationCode, Long jobRoleId,
			Integer experience, Integer salary, String jobDescription, String requirementsCandidate,
			String benefitCandidate, LocalDate deadlineApplication);
	void deleteJobRequest(JobRequest jobRequest);
	List<JobRequest> findAllByOrganizationId(Long id);
	JobRequestJDBC createJobRequestJdbc(String title, int locationCode, Long jobRoleId, 
	        Integer experience, Integer salary, Long organizationId, String jobDescription, 
	        String requirementsCandidate, String benefitCandidate, LocalDate deadlineApplication);
	JobRequestJDBC updateJobRequestJdbc(JobRequestJDBC jobRequest, String title, int locationCode, Long jobRoleId,
			Integer experience, Integer salary, String jobDescription, String requirementsCandidate,
			String benefitCandidate, LocalDate deadlineApplication);
	void deleteJobRequestJdbc(JobRequestJDBC jobRequest);
}