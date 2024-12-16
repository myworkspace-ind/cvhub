package mks.myworkspace.cvhub.repository;




import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.myworkspace.cvhub.entity.JobRole;

@Repository
public interface JobRoleRepository extends JpaRepository<JobRole, Long> {
	Page<JobRole> findAll(Pageable pageable);
	
	boolean existsByTitle(String title);
}
