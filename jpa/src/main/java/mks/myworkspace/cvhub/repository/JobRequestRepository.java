package mks.myworkspace.cvhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.myworkspace.cvhub.entity.JobRequest;
import org.springframework.data.domain.*;

@Repository
public interface JobRequestRepository extends JpaRepository<JobRequest, Long> {
	Page<JobRequest> findAll(Pageable pageable);
}
