package mks.myworkspace.cvhub.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import mks.myworkspace.cvhub.entity.JobRole;
import mks.myworkspace.cvhub.model.JobRoleJDBC;
import mks.myworkspace.cvhub.repository.JobRoleRepository;
@Service
public interface JobRoleService {
	JobRoleRepository getRepo();
	
	JobRole createJobRole(String title, String description);
	JobRole updateJobRole(JobRole job, String title, String description);
	void deleteJobRole(JobRole job);
	Page<JobRole> getAllJobRole(Pageable pageable);
	boolean existsByTitle(String title);
	boolean canEditByTitle(String title);

	JobRoleJDBC createJobRoleJdbc(JobRoleJDBC jobRoleJDBC);

	void updateJobRoleJdbc(JobRoleJDBC jobRoleJDBC);

	JobRoleJDBC getJobRoleById(Long id);

	void deleteJobRoleJdbc(JobRoleJDBC jobRoleJdbc);
}
