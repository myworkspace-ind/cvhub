package mks.myworkspace.cvhub.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mks.myworkspace.cvhub.model.JobRoleJDBC;

@Repository
public class JobRoleDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JobRoleJDBC save(JobRoleJDBC jobRole) {
        String sql = "INSERT INTO cvhub_jobrole (title, description, created_dte, modified_dte) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, jobRole.getTitle());
            ps.setString(2, jobRole.getDescription());
            ps.setTimestamp(3, new java.sql.Timestamp(jobRole.getCreatedDate().getTime()));
            ps.setTimestamp(4, new java.sql.Timestamp(jobRole.getModifiedDate().getTime()));
            return ps;
        }, keyHolder);
        
        jobRole.setId(keyHolder.getKey().longValue());
        return jobRole;
    }

    public void delete(Long id) {
        String sql = "DELETE FROM cvhub_jobrole WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}