package mks.myworkspace.cvhub.repository;


import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mks.myworkspace.cvhub.entity.CV;
import mks.myworkspace.cvhub.entity.User;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);
	
	@Query("SELECT u FROM User u WHERE u.email = :email")
	User findUserByEmail(@Param("email")String email);
	
	@Query("SELECT u FROM User u WHERE u.createdDate >= :startOfPeriod AND u.role = 'ROLE_USER'")
	Page<User> findUsersByCreationDateWithRoleUser(@Param("startOfPeriod") Date startOfPeriod, Pageable pageable);
	
	@Query("SELECT DISTINCT u FROM User u JOIN u.cvList c WHERE c IS NOT NULL")
    Page<User> findUsersWithCVs(Pageable pageable);
	
	@Query("SELECT u FROM User u JOIN u.cvList c WHERE c.id = :cvId")
	Optional<User> findUserByCVId(@Param("cvId") Long cvId);
	
	// Tim kiem va phan trang theo ten, email, sdt 
    @Query("SELECT c FROM User c WHERE " +
            "LOWER(c.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.phone) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<User> search(@Param("keyword") String keyword, Pageable pageable);
    
 // Truy vấn số lượng người dùng theo tháng và năm
    @Query("SELECT COUNT(u) FROM User u WHERE MONTH(u.createdDate) = :month AND YEAR(u.createdDate) = :year")
    Long getUserCountPerMonth(@Param("month") int month, @Param("year") int year);
    
    // Có thể cần phương thức trả về danh sách thống kê cho tất cả các tháng trong năm:
    @Query("SELECT MONTH(u.createdDate), COUNT(u) FROM User u WHERE YEAR(u.createdDate) = :year GROUP BY MONTH(u.createdDate)")
    List<Object[]> getUserCountPerYear(@Param("year") int year);

}