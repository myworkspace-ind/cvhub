package mks.myworkspace.cvhub.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import mks.myworkspace.cvhub.entity.JobSaved;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
@Service
public interface JobSavedService {
	void AddJobSaved(User user, JobRequest jobRequest);

	List<JobSaved> getJobSavedByUser(User currentUser);
	boolean hasJobSaved(User user, Long jobRequestId);
	List<JobSaved> getJobSavedByJobRequest(JobRequest jobRequest);
	List<JobSaved> findAll();
	Page<JobSaved> findAll(PageRequest pageRequest);
	Page<JobSaved> findByCreatedDateBetween(Date start, Date end, PageRequest pageRequest);
	List<JobSaved> findByCreatedDateBetween(Date start, Date end);
	void deleteJobSavedById(Long id);
	JobSaved getJobSavedByJobSavedId(Long id);
}
