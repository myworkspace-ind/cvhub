package mks.myworkspace.cvhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.myworkspace.cvhub.entity.SakaiUserIdMap;

@Repository
public interface SakaiUserIdMapRepository extends JpaRepository<SakaiUserIdMap, String> {
	
}
