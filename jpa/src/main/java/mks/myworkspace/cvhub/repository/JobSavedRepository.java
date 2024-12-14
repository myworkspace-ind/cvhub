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
import mks.myworkspace.cvhub.entity.JobSaved;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.User;

@Repository
public interface JobSavedRepository extends JpaRepository<JobSaved, Long> {
	@Query("SELECT ja FROM JobSaved ja WHERE ja.user = :user")
	List<JobSaved> findByUser(@Param("user") User user);

	@Query("SELECT ja FROM JobSaved ja WHERE ja.jobRequest = :jobRequest AND ja.user = :user")
	List<JobSaved> findByUserAndJobRequest(@Param("jobRequest") JobRequest jobRequest);

	@Query("SELECT ja FROM JobSaved ja WHERE ja.id = :id")
	Optional<JobSaved> findByJobSavedId(@Param("id") Long id);

	@Query("SELECT j FROM JobSaved j WHERE j.createdAt >= :startDate AND j.createdAt <= :endDate")
    List<JobSaved> findByCreatedDateBetween(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
	
	@Query("SELECT j FROM JobSaved j WHERE j.createdAt >= :startDate AND j.createdAt <= :endDate")
    Page<JobSaved> findByCreatedDateBetween(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("pageRequest") org.springframework.data.domain.Pageable pageable);
	
	@Query("SELECT COUNT(ja.id) FROM JobSaved ja WHERE ja.jobRequest.organization.id = :organizationId")
	Long getJobSavedCountByOrgId(@Param("organizationId") Long organizationId);				// #organizationReport
	@Query("SELECT ja FROM JobSaved ja " +
		       "JOIN ja.jobRequest jr " +
		       "WHERE jr.organization.id = :organizationId")
	List<JobSaved> findJobSavedByOrgId(@Param("organizationId") Long organizationId);	// #organizationReport
}
