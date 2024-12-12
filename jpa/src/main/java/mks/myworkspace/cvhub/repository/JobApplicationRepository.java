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
import mks.myworkspace.cvhub.entity.JobApplication;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.User;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
	@Query("SELECT ja FROM JobApplication ja WHERE ja.user = :user")
	List<JobApplication> findByUser(@Param("user") User user);

	@Query("SELECT ja FROM JobApplication ja WHERE ja.jobRequest = :jobRequest")
	List<JobApplication> findByUserAndJobRequest(@Param("jobRequest") JobRequest jobRequest);

	@Query("SELECT ja FROM JobApplication ja WHERE ja.id = :id")
	Optional<JobApplication> findByJobApplicationId(@Param("id") Long id);

	@Query("SELECT ja FROM JobApplication ja " +
		       "JOIN ja.jobRequest jr " +
		       "WHERE ja.status = :status " +
		       "AND jr.organization.id = :organizationId")
	List<JobApplication> findJobApplicationByStatusAndOrganizationId(
		    @Param("status") String status, 
		    @Param("organizationId") Long organizationId);
	
	@Query("SELECT j FROM JobApplication j WHERE j.createdDate >= :startDate AND j.createdDate <= :endDate")
    List<JobApplication> findByCreatedDateBetween(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
	
	@Query("SELECT j FROM JobApplication j WHERE j.createdDate >= :startDate AND j.createdDate <= :endDate")
    Page<JobApplication> findByCreatedDateBetween(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("pageRequest") org.springframework.data.domain.Pageable pageable);
	
	@Query("SELECT COUNT(ja.id) FROM JobApplication ja WHERE ja.jobRequest.organization.id = :organizationId")
	Long getApplicantCountByOrgId(@Param("organizationId") Long organizationId);		// #organizationReport
}
