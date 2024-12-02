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
		       + "WHERE "
		       + "( :locationCD IS NULL OR :locationCD <= 0 OR j.location.code = :locationCD) AND "
		       + "( j.jobRole.id IN :industryCD) AND "
		       + "( :company IS NULL OR j.organization.title LIKE CONCAT('%', :company, '%') OR :bool = false) AND "
		       + "( :jobName IS NULL OR j.title LIKE CONCAT('%', :jobName, '%') OR :bool = true)")
		Page<JobRequest> findBySearchCriteria(@Param("locationCD") Integer locationCD,
		                                      @Param("industryCD") List<Long> selectedIndustries,
		                                      @Param("company") String company,
		                                      @Param("jobName") String jobName,
		                                      @Param("bool") boolean search, Pageable pageable);

}
