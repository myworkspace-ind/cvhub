package mks.myworkspace.cvhub.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import mks.myworkspace.cvhub.model.OrganizationReviewJDBC;

@Component
public class OrganizationReviewDao {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    // Create new review
    public OrganizationReviewJDBC create(OrganizationReviewJDBC review) {
        String sql = "INSERT INTO cvhub_organization_review (organization_id, user_id, rating, review_text, created_date, modified_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, review.getOrganizationId());
            ps.setLong(2, review.getUserId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getReviewText());
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis())); // Set created_date
            ps.setTimestamp(6, new Timestamp(System.currentTimeMillis())); // Set modified_date
            return ps;
        }, keyHolder);
        
        review.setId(keyHolder.getKey().longValue());
        return findById(review.getId());
    }
    
    // Find review by ID
    public OrganizationReviewJDBC findById(Long id) {
        String sql = "SELECT * FROM cvhub_organization_review WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, 
                (rs, rowNum) -> OrganizationReviewJDBC.fromResultSet(rs), 
                id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    // Find reviews by organization
    public List<OrganizationReviewJDBC> findByOrganizationId(Long organizationId) {
        String sql = "SELECT * FROM cvhub_organization_review WHERE organization_id = ?";
        return jdbcTemplate.query(sql, 
            (rs, rowNum) -> OrganizationReviewJDBC.fromResultSet(rs), 
            organizationId);
    }
    
    // Find review by user and organization
    public OrganizationReviewJDBC findByUserAndOrganization(Long userId, Long organizationId) {
        String sql = "SELECT * FROM cvhub_organization_review WHERE organization_id = ? AND user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, 
                (rs, rowNum) -> OrganizationReviewJDBC.fromResultSet(rs), 
                organizationId, userId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    // Update review
    public void update(OrganizationReviewJDBC review) {
        String sql = "UPDATE cvhub_organization_review SET rating = ?, review_text = ?, modified_date = ? " +
                     "WHERE id = ? AND user_id = ?";
        int affectedRows = jdbcTemplate.update(sql, 
            review.getRating(), 
            review.getReviewText(),
            new Timestamp(System.currentTimeMillis()), // Set modified_date 
            review.getId(), 
            review.getUserId());
        
        if (affectedRows == 0) {
            throw new RuntimeException("Update failed: Review not found or user not authorized");
        }
    }
    
    // Delete review
    public void delete(Long id, Long userId) {
        String sql = "DELETE FROM cvhub_organization_review WHERE id = ? AND user_id = ?";
        int affectedRows = jdbcTemplate.update(sql, id, userId);
        
        if (affectedRows == 0) {
            throw new RuntimeException("Delete failed: Review not found or user not authorized");
        }
    }
}