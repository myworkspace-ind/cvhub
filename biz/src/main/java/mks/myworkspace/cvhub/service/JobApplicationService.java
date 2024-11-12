package mks.myworkspace.cvhub.service;

import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.repository.JobApplicationRepository;

public interface JobApplicationService {
	void AddJobApplication(User user, JobRequest jobRequest);
}
