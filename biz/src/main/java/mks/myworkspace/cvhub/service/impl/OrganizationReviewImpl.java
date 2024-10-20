package mks.myworkspace.cvhub.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mks.myworkspace.cvhub.entity.OrganizationReview;
import mks.myworkspace.cvhub.repository.OrganizationReviewRepository;
import mks.myworkspace.cvhub.service.OrganizationReviewService;

@Service
@RequiredArgsConstructor
public class OrganizationReviewImpl implements OrganizationReviewService {
	private final OrganizationReviewRepository repo;
	
	@Override
	public OrganizationReview createReview(OrganizationReview organizationReview) {
		return repo.save(organizationReview);
	}

	@Override
	public void deleteReview(long id) {
		if(repo.findById(id).isPresent()) repo.deleteById(id);
	}

	@Override
	public List<OrganizationReview> getReviews() {
		return repo.findAll();
	}
}
