package mks.myworkspace.cvhub.entity.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.myworkspace.cvhub.entity.JobRole;

@Repository
public interface JobRoleRepository extends JpaRepository<JobRole, Long> {

	List<JobRole> findByIndustry(String industry);
}
