package mks.myworkspace.cvhub.repository;

import java.awt.print.Pageable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mks.myworkspace.cvhub.entity.CV;
import mks.myworkspace.cvhub.entity.FollowedOrganization;
import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.entity.User;

@Repository
public interface FollowedOrganizationRepository extends JpaRepository<FollowedOrganization, Long> {
	@Query("SELECT ja FROM FollowedOrganization ja WHERE ja.user = :user")
	List<FollowedOrganization> findByUser(@Param("user") User user);

	@Query("SELECT ja FROM FollowedOrganization ja WHERE ja.organization = :organization AND ja.user = :user")
	List<FollowedOrganization> findByUserAndOrganization(@Param("organization") Organization organization);

	@Query("SELECT ja FROM FollowedOrganization ja WHERE ja.id = :id")
	Optional<FollowedOrganization> findByFollowedOrganizationId(@Param("id") Long id);

	@Query("SELECT j FROM FollowedOrganization j WHERE j.createdAt >= :startDate AND j.createdAt <= :endDate")
    List<FollowedOrganization> findByCreatedDateBetween(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
	
	@Query("SELECT j FROM FollowedOrganization j WHERE j.createdAt >= :startDate AND j.createdAt <= :endDate")
    Page<FollowedOrganization> findByCreatedDateBetween(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("pageRequest") org.springframework.data.domain.Pageable pageable);

	void deleteByUserAndOrganization(User user, Organization organization);
	boolean existsByUserAndOrganization(User user, Organization organization);
}
