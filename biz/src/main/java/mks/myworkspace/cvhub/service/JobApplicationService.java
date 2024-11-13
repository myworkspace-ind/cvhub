package mks.myworkspace.cvhub.service;

import java.util.List;

import mks.myworkspace.cvhub.entity.JobApplication;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.repository.JobApplicationRepository;

public interface JobApplicationService {
	void AddJobApplication(User user, JobRequest jobRequest);

	List<JobApplication> getApplicationsByUser(User currentUser);
	boolean hasUserApplied(User user, Long jobRequestId);
}
