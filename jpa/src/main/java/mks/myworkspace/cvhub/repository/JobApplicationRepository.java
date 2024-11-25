package mks.myworkspace.cvhub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mks.myworkspace.cvhub.entity.JobApplication;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.User;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
	@Query("SELECT ja FROM JobApplication ja WHERE ja.user = :user")
    List<JobApplication> findByUser(@Param("user") User user);
	
	@Query("SELECT ja FROM JobApplication ja WHERE ja.jobRequest = :jobRequest")
    List<JobApplication> findByUserAndJobRequest(@Param("jobRequest") JobRequest jobRequest);
	
	@Query("SELECT ja FROM JobApplication ja WHERE ja.jobRequest.id = :jobRequestId")
	List<JobApplication> findByJobRequestId(@Param("jobRequestId") Long jobRequestId);

}
