package mks.myworkspace.cvhub.repository;

import mks.myworkspace.cvhub.entity.JobApplication;
import mks.myworkspace.cvhub.entity.JobRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRequestRepository_tuan_22110450 extends JpaRepository<JobRequest, Long> {
    @Query("SELECT j FROM JobRequest j "
            + " WHERE "
            + "( :locationCD IS NULL OR :locationCD <= 0 OR j.location.code = :locationCD) AND "
            + "( :industryCD IS NULL OR :industryCD <= 0L OR j.jobRole.id = :industryCD) AND "
            + "( :company IS NULL OR j.organization.title LIKE %:company% OR :bool is false) AND "
            + "( :jobName IS NULL OR j.title LIKE %:jobName% OR :bool is true)")
    Page<JobRequest> findBySearchCriteria(
            @Param("locationCD") int locationCD,
            @Param("industryCD") Long industryCD,
            @Param("company") String company,
            @Param("jobName") String jobName,
            @Param("bool") boolean search,
            Pageable pageable);
}
