package mks.myworkspace.cvhub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.myworkspace.cvhub.entity.JobRole;
import org.springframework.data.domain.*;

@Repository
public interface JobRoleRepository extends JpaRepository<JobRole, Long> {

	List<JobRole> findByIndustry(String industry);
	Page<JobRole> findAll(Pageable pageable);
}
