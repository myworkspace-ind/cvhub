package mks.myworkspace.cvhub.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.Getter;
import mks.myworkspace.cvhub.entity.JobRole;
import mks.myworkspace.cvhub.repository.JobRoleRepository;
import mks.myworkspace.cvhub.service.JobRoleService;

@Service
public class JobRoleImpl implements JobRoleService {

	@Getter
	@Autowired
	JobRoleRepository repo;

	@Override
	public JobRole createJobRole(String title, String description) {
		
		//kiem tra cac tham so dau vao
		if(title == null || description.isEmpty()) {
			throw new IllegalArgumentException("Title and Description must not be null or empty.");
		}
		JobRole jobRole = new JobRole();
		jobRole.setTitle(title);
		jobRole.setDescription(description);
		
		return jobRole;
	}

	@Override
	public JobRole updateJobRole(JobRole job, String title, String description) {
		job.setTitle(title);
		job.setDescription(description);
		
		return getRepo().save(job);
	}

	@Override
	public void deleteJobRole(JobRole job) {
		if(job == null) {
			throw new IllegalArgumentException("JobRole must not be null.");
		}
		getRepo().delete(job);
	}

	@Override
	public Page<JobRole> getAllJobRole(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
}
