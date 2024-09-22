package mks.myworkspace.cvhub.service;

import org.springframework.stereotype.Service;

import mks.myworkspace.cvhub.repository.JobRoleRepository;
@Service
public interface JobRoleService {
	JobRoleRepository getRepo();

}