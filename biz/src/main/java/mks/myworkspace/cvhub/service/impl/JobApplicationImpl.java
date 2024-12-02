
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
import mks.myworkspace.cvhub.entity.JobApplication;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.repository.JobApplicationRepository;
import mks.myworkspace.cvhub.repository.JobRequestRepository;
import mks.myworkspace.cvhub.repository.OrganizationReviewRepository;
import mks.myworkspace.cvhub.service.JobApplicationService;

@Service
@RequiredArgsConstructor
public class JobApplicationImpl implements JobApplicationService{

	private final JobApplicationRepository repo;
	@Override
	public void AddJobApplication(User user, JobRequest jobRequest) {
		JobApplication jobApplication = new JobApplication();
		jobApplication.setJobRequest(jobRequest);

        jobApplication.setUser(user);
        jobApplication.setStatus("PENDING");
        jobApplication.setNote(null);
        repo.save(jobApplication);

	}
	@Override
	public List<JobApplication> getApplicationsByUser(User currentUser) {
		// TODO Auto-generated method stub
		return repo.findByUser(currentUser);
	}
	@Override
	public boolean hasUserApplied(User user, Long jobRequestId) {
		List<JobApplication> applications = getApplicationsByUser(user);
	    return applications.stream().anyMatch(app -> app.getJobRequest().getId().equals(jobRequestId));
	}
	@Override
	public List<JobApplication> getApplicationsByJobRequest(JobRequest jobRequest) {
		return repo.findByUserAndJobRequest(jobRequest);
	}
	@Override
	public List<JobApplication> findAll() {
		// TODO Auto-generated method stub
		return repo.findAll();
	}

	@Override
	public Page<JobApplication> findAll(PageRequest pageRequest) {
		return repo.findAll(pageRequest);
	}

	@Override
	public Page<JobApplication> findByCreatedDateBetween(Date start, Date end, PageRequest pageRequest) {
		return repo.findByCreatedDateBetween(start, end, pageRequest);
	}

	@Override
	public List<JobApplication> findByCreatedDateBetween(Date start, Date end) {
		return repo.findByCreatedDateBetween(start, end);
	}

	@Override
	public void deleteApplicationById(Long id) {
		repo.deleteById(id);
		
	}
	@Override
	public JobApplication getApplicationsByJobApplicationId(Long id) {
		// TODO Auto-generated method stub
		return repo.findByJobApplicationId(id).get();
	}
	@Override
	public List<JobApplication> findJobApplicationByOption(Long organizationId, String option) {
		return repo.findJobApplicationByStatusAndOrganizationId(option, organizationId);
	}
}

