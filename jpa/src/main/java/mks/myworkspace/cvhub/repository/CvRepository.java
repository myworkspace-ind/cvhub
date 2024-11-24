package mks.myworkspace.cvhub.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mks.myworkspace.cvhub.entity.CV;


public interface CvRepository extends JpaRepository<CV, Long> {
	@Query("SELECT o.logo FROM CV o WHERE o.logoID = :logoId")
	byte[] getImageByLogoId(@Param("logoId") UUID logoId);
	@Query("SELECT COUNT(cv) FROM CV cv WHERE cv.user.id = :userId AND cv.selected = true")
	long getSelectedCVCount(@Param("userId") Long userId);
	@Query("SELECT c FROM CV c WHERE c.user.id = :userId ORDER BY c.createdDate DESC")
	List<CV> findCVsByUserId(@Param("userId") Long userId);
	@Query("SELECT cv FROM CV cv WHERE cv.id = :cvId AND cv.user.id = :userId")
    Optional<CV> findByIdAndUserId(@Param("cvId") Long cvId, @Param("userId") Long userId);
	@Query("SELECT COUNT(cv) FROM CV cv WHERE cv.user.id = :userId AND cv.selected = true")
    int countByUserIdAndSelectedTrue(@Param("userId") Long userId);
	// Lấy danh sách CV của user
    @Query("SELECT cv FROM CV cv WHERE cv.user.id = :userId ORDER BY cv.modifiedDate DESC")
    List<CV> findByUserId(@Param("userId") Long userId);
    
    // Lấy danh sách CV đã chọn của user
    @Query("SELECT cv FROM CV cv WHERE cv.user.id = :userId AND cv.selected = true ORDER BY cv.modifiedDate DESC")
    List<CV> findSelectedCVsByUserId(@Param("userId") Long userId);
    
    // Kiểm tra số lượng CV đã chọn của user có vượt quá limit không
    @Query("SELECT CASE WHEN COUNT(cv) >= :limit THEN true ELSE false END FROM CV cv WHERE cv.user.id = :userId AND cv.selected = true")
    boolean hasReachedSelectedLimit(@Param("userId") Long userId, @Param("limit") int limit);

    // Optional: Tìm CV mới nhất của user
    @Query("SELECT cv FROM CV cv WHERE cv.user.id = :userId ORDER BY cv.modifiedDate DESC")
    Optional<CV> findLatestCVByUserId(@Param("userId") Long userId);
    
    // Optional: Tìm CV theo jobRole và user
    @Query("SELECT cv FROM CV cv WHERE cv.user.id = :userId AND cv.jobRole.id = :jobRoleId")
    List<CV> findByUserIdAndJobRole(@Param("userId") Long userId, @Param("jobRoleId") Long jobRoleId);

    @Query("SELECT cv FROM CV cv WHERE cv.user.id = :userId AND cv.isprimary = true")
    CV findPrimaryByUserId(@Param("userId") Long userId);
    
}