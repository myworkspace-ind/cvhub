package mks.myworkspace.cvhub.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import mks.myworkspace.cvhub.entity.JobApplication;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface JobApplicationService {
	void AddJobApplication(User user, JobRequest jobRequest);

	List<JobApplication> getApplicationsByUser(User currentUser);
	boolean hasUserApplied(User user, Long jobRequestId);
	List<JobApplication> getApplicationsByJobRequest(JobRequest jobRequest);
	List<JobApplication> findAll();
	Page<JobApplication> findAll(PageRequest pageRequest);
	Page<JobApplication> findByCreatedDateBetween(Date start, Date end, PageRequest pageRequest);
	List<JobApplication> findByCreatedDateBetween(Date start, Date end);
	void deleteApplicationById(Long id);
}
