package mks.myworkspace.cvhub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.myworkspace.cvhub.entity.JobRequest;

@Repository
public interface JobRoleRepository extends JpaRepository<JobRequest, Long> {

	List<JobRequest> findByIndustry(String industry);
}
