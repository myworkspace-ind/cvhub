package mks.myworkspace.cvhub.model;

import java.sql.Date;
import java.sql.SQLException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationReviewJDBC {
    private Long id;
    private Long organizationId;
    private Long userId;
    private Integer rating;
    private String reviewText;
    private Date createdDate;
    private Date modifiedDate;
    private String userName;
    
    // Constructor cho tạo mới review
    public OrganizationReviewJDBC(Long organizationId, Long userId, Integer rating, String reviewText) {
        this.organizationId = organizationId;
        this.userId = userId;
        this.rating = rating;
        this.reviewText = reviewText;
    }
    
    // Helper method để tạo object từ ResultSet
    public static OrganizationReviewJDBC fromResultSet(java.sql.ResultSet rs) throws java.sql.SQLException {
        OrganizationReviewJDBC review = new OrganizationReviewJDBC();
        review.setId(rs.getLong("id"));
        review.setOrganizationId(rs.getLong("organization_id"));
        review.setUserId(rs.getLong("user_id"));
        review.setRating(rs.getInt("rating"));
        review.setReviewText(rs.getString("review_text"));
        review.setCreatedDate(rs.getDate("created_date"));
        review.setModifiedDate(rs.getDate("modified_date"));
        
        // Nếu có join với bảng user để lấy tên
        try {
            review.setUserName(rs.getString("full_name"));
        } catch (SQLException e) {
            // Không có column full_name trong result set
        }
        
        return review;
    }
    
    // SQL Queries
    public static final String SQL_INSERT = 
        "INSERT INTO cvhub_organization_review (organization_id, user_id, rating, review_text, created_date, modified_date) " +
        "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        
    public static final String SQL_UPDATE = 
        "UPDATE cvhub_organization_review " +
        "SET rating = ?, review_text = ?, modified_date = CURRENT_TIMESTAMP " +
        "WHERE id = ? AND user_id = ?";  // Thêm điều kiện user_id để đảm bảo chỉ update review của chính user
        
    public static final String SQL_DELETE = 
        "DELETE FROM cvhub_organization_review WHERE id = ? AND user_id = ?";  // Thêm điều kiện user_id
        
    public static final String SQL_SELECT_BY_ID = 
        "SELECT r.*, u.full_name " +
        "FROM cvhub_organization_review r " +
        "JOIN cvhub_user u ON r.user_id = u.id " +
        "WHERE r.id = ?";
        
    public static final String SQL_SELECT_BY_ORGANIZATION = 
        "SELECT r.*, u.full_name " +
        "FROM cvhub_organization_review r " +
        "JOIN cvhub_user u ON r.user_id = u.id " +
        "WHERE r.organization_id = ? " +
        "ORDER BY r.created_date DESC";
        
    public static final String SQL_SELECT_BY_USER_AND_ORGANIZATION = 
        "SELECT r.*, u.full_name " +
        "FROM cvhub_organization_review r " +
        "JOIN cvhub_user u ON r.user_id = u.id " +
        "WHERE r.organization_id = ? AND r.user_id = ?";
}