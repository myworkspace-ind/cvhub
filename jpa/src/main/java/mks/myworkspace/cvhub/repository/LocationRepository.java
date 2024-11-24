package mks.myworkspace.cvhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.myworkspace.cvhub.entity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location,Integer> {
	
}