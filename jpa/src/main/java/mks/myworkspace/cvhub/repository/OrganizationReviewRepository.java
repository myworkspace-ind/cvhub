package mks.myworkspace.cvhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.myworkspace.cvhub.entity.OrganizationReview;

@Repository
public interface OrganizationReviewRepository extends JpaRepository<OrganizationReview, Long> {

}
