package mks.myworkspace.cvhub.service;

import java.util.List;

import mks.myworkspace.cvhub.entity.OrganizationReview;

public interface OrganizationReviewService {
	OrganizationReview createReview(OrganizationReview organizationReview);
	List<OrganizationReview> getReviews();
	void deleteReview(long id);
}
