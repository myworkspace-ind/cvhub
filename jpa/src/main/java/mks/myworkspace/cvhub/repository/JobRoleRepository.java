package mks.myworkspace.cvhub.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.JobRole;

@Repository
public interface JobRoleRepository extends JpaRepository<JobRole, Long> {

}
