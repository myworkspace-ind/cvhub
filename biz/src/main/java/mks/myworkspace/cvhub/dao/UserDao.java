package mks.myworkspace.cvhub.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import mks.myworkspace.cvhub.model.CV;
import mks.myworkspace.cvhub.model.Organization;
import mks.myworkspace.cvhub.model.UserJDBC;

@Component
public class UserDao {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Create
    public UserJDBC save(UserJDBC user) {
        String sql = "INSERT INTO cvhub_user (fullname, image, email, password, phone, role, created_date, modified_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getFullName());
            ps.setBytes(2, user.getImage());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getRole());
            return ps;
        }, keyHolder);
        
        user.setId(keyHolder.getKey().longValue());
        return user;
    }
    
 // Delete
    public boolean delete(Long id) {
        String sql = "DELETE FROM cvhub_user WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }
}