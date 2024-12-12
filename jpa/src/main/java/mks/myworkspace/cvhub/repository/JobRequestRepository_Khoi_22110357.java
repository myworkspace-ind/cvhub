package mks.myworkspace.cvhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mks.myworkspace.cvhub.entity.JobRequest;

@Repository
public interface JobRequestRepository_Khoi_22110357 extends JpaRepository<JobRequest, Long> {
    @Query("SELECT COUNT(j) FROM JobRequest j WHERE j.organization.id = :organizationId")
    Long countJobPostingsByOrganizationId(@Param("organizationId") Long organizationId);
}
