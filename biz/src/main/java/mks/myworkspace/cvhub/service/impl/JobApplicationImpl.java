package mks.myworkspace.cvhub.service.impl;

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
}
