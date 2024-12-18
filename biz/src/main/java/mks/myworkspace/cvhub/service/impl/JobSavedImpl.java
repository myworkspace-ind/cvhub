
package mks.myworkspace.cvhub.service.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mks.myworkspace.cvhub.entity.JobSaved;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.repository.JobSavedRepository;
import mks.myworkspace.cvhub.repository.JobRequestRepository;
import mks.myworkspace.cvhub.repository.OrganizationReviewRepository;
import mks.myworkspace.cvhub.service.JobSavedService;

@Service
@RequiredArgsConstructor
public class JobSavedImpl implements JobSavedService{

	private final JobSavedRepository repo;
	@Override
	public void AddJobSaved(User user, JobRequest jobRequest) {
		JobSaved jobSaved = new JobSaved();
		jobSaved.setJobRequest(jobRequest);

		jobSaved.setUser(user);
        repo.save(jobSaved);

	}
	@Override
	public List<JobSaved> getJobSavedByUser(User currentUser) {
		// TODO Auto-generated method stub
		return repo.findByUser(currentUser);
	}
	@Override
	public boolean hasJobSaved(User user, Long jobRequestId) {
		List<JobSaved> jobsaved = getJobSavedByUser(user);
	    return jobsaved.stream().anyMatch(app -> app.getJobRequest().getId().equals(jobRequestId));
	}
	@Override
	public List<JobSaved> getJobSavedByJobRequest(JobRequest jobRequest) {
		return repo.findByUserAndJobRequest(jobRequest);
	}
	@Override
	public List<JobSaved> findAll() {
		// TODO Auto-generated method stub
		return repo.findAll();
	}

	@Override
	public Page<JobSaved> findAll(PageRequest pageRequest) {
		return repo.findAll(pageRequest);
	}

	@Override
	public Page<JobSaved> findByCreatedDateBetween(Date start, Date end, PageRequest pageRequest) {
		return repo.findByCreatedDateBetween(start, end, pageRequest);
	}

	@Override
	public List<JobSaved> findByCreatedDateBetween(Date start, Date end) {
		return repo.findByCreatedDateBetween(start, end);
	}

	@Override
	public void deleteJobSavedById(Long id) {
		repo.deleteById(id);
		
	}
	@Override
	public JobSaved getJobSavedByJobSavedId(Long id) {
		// TODO Auto-generated method stub
		return repo.findByJobSavedId(id).get();
	}
}

