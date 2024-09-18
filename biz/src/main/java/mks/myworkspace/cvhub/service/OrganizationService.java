package mks.myworkspace.cvhub.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import mks.myworkspace.cvhub.repository.OrganizationRepository;

@Service
public interface OrganizationService {
	OrganizationRepository getRepo();

}
