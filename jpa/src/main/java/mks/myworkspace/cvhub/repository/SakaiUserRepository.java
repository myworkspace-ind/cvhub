package mks.myworkspace.cvhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.myworkspace.cvhub.entity.SakaiUser;

@Repository
public interface SakaiUserRepository extends JpaRepository<SakaiUser, String> {
}
