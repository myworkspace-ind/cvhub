package mks.myworkspace.cvhub.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import mks.myworkspace.cvhub.repository.JobRoleRepository;
import mks.myworkspace.cvhub.service.JobRoleService;

@Service
public class JobRoleServiceImpl implements JobRoleService {

	@Getter
	@Autowired
	JobRoleRepository repo;

}
