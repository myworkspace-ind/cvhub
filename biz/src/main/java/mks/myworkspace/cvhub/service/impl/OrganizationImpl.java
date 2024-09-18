package mks.myworkspace.cvhub.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import mks.myworkspace.cvhub.repository.OrganizationRepository;
import mks.myworkspace.cvhub.service.OrganizationService;

@Service
public class OrganizationImpl implements OrganizationService {

	@Getter
	@Autowired
	OrganizationRepository repo;
}
