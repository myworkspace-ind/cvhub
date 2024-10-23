package mks.myworkspace.cvhub.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mks.myworkspace.cvhub.entity.CV;


public interface CvRepository extends JpaRepository<CV, Long> {
	@Query("SELECT o.logo FROM CV o WHERE o.logoID = :logoId")
	byte[] getImageByLogoId(@Param("logoId") UUID logoId);
}