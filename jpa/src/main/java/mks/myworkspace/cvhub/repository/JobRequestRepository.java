package mks.myworkspace.cvhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mks.myworkspace.cvhub.entity.JobRequest;

public interface JobRequestRepository extends JpaRepository<JobRequest, Long> {

}
