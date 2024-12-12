package mks.myworkspace.cvhub.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mks.myworkspace.cvhub.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization,Long> {
	@Query("SELECT o.logo FROM Organization o WHERE o.logoID = :logoId")
	byte[] getImageByLogoId(@Param("logoId") UUID logoId);
	@Query("SELECT o.id From Organization o WHERE o.title = :title")
	Long getIdByTitle(@Param("title") String title);
	@Query("SELECT o FROM Organization o WHERE o.user.id = :userId")
	Organization findByUserId(@Param("userId") Long userId);
	@Query("SELECT o FROM Organization o WHERE LOWER(o.title) LIKE LOWER(CONCAT('%', :title, '%'))")
	List<Organization> searchByTitle(@Param("title") String title); // them boi LeDaoNhanSam tim cong ty bang ten cong ty
	Page<Organization> findAll(@Param("pageRequest") Pageable pageRequest );
	List<Organization> findByTitleContaining(String title);
	Page<Organization> findByTitleContaining(@Param("pageRequest") Pageable pageRequest, String title);
	@Query("SELECT o FROM Organization o WHERE o.createdDate >= :startDate")
    Page<Organization> findAllCreatedDateStartFrom(@Param("startDate") Date startDate, @Param("pageRequest") Pageable pageable);	// #organizationReport
	@Query("SELECT COUNT(jr) FROM JobRequest jr WHERE jr.organization.id = :organizationId")
	Long countByOrganizationId(@Param("organizationId") Long organizationId);
	@Query("SELECT o, COUNT(jr) AS jobCount FROM Organization o LEFT JOIN JobRequest jr ON o.id = jr.organization.id GROUP BY o.id ORDER BY o.createdDate ASC")
	List<Organization> findAllByOrderByCreatedDateAsc();
	@Query("SELECT o, COUNT(jr) AS jobCount FROM Organization o LEFT JOIN JobRequest jr ON o.id = jr.organization.id GROUP BY o.id ORDER BY o.createdDate DESC")
	List<Organization> findAllByOrderByCreatedDateDesc();
	@Query("SELECT o, COUNT(jr) AS jobCount FROM Organization o LEFT JOIN JobRequest jr ON o.id = jr.organization.id GROUP BY o.id ORDER BY jobCount ASC")
	List<Organization> findAllByOrderByJobCountAsc();
	@Query("SELECT o, COUNT(jr) AS jobCount FROM Organization o LEFT JOIN JobRequest jr ON o.id = jr.organization.id GROUP BY o.id ORDER BY jobCount DESC")
	List<Organization> findAllByOrderByJobCountDesc();
}
