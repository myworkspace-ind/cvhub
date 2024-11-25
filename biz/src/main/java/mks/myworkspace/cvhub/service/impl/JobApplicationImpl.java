package mks.myworkspace.cvhub.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
        jobApplication.setStatus(null);
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
	public List<JobApplication> findAll() {
		// TODO Auto-generated method stub
		return repo.findAll();
	}
	@Override
	public void deleteApplicationById(Long id) {
		repo.deleteById(id);
		
	}
	public List<JobApplication> getApplicationsForJobRequest(Long jobRequestId) {
		JobRequest jobRequest = new JobRequest();
		jobRequest.setId(jobRequestId); // Tạo đối tượng JobRequest với jobRequestId
		return repo.findByUserAndJobRequest(jobRequest);
	}
}
