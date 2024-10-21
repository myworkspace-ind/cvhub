package mks.myworkspace.cvhub.service;

import java.util.List;

import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.entity.OrganizationReview;

public interface OrganizationReviewService {
	OrganizationReview createReview(OrganizationReview organizationReview);
	List<OrganizationReview> getReviews();
	List<OrganizationReview> getReviewsByOrganizationId(long organizationId);
	void deleteReview(long id);
}
