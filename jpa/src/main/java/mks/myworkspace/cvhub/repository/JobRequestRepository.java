package mks.myworkspace.cvhub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.Organization;

import org.springframework.stereotype.Repository;

import mks.myworkspace.cvhub.entity.JobRequest;
import org.springframework.data.domain.*;


@Repository
public interface JobRequestRepository extends JpaRepository<JobRequest, Long> {

	  @Query("SELECT jr FROM JobRequest jr WHERE jr.organization.id = :organizationId")
	    List<JobRequest> findByOrganizationId(@Param("organizationId") Long organizationId);
	Page<JobRequest> findAll(Pageable pageable);

}
