package mks.myworkspace.cvhub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mks.myworkspace.cvhub.entity.JobRequest;
public interface JobRequestRepository extends JpaRepository<JobRequest, Long> {

	  @Query("SELECT jr FROM JobRequest jr WHERE jr.organization.id = :organizationId")
	    List<JobRequest> findByOrganizationId(@Param("organizationId") Long organizationId);
//	  @Query("SELECT jr FROM JobRequest jr WHERE jr.id = :id")
	    Optional<JobRequest> findById(@Param("id") Long id);
	    Page<JobRequest> findAll(@Param("pageRequest") Pageable pageRequest );
	    JobRequest findByTitle(@Param("title") String title);
}
