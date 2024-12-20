package mks.myworkspace.cvhub.dao;

import java.sql.PreparedStatement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.UUID;
import java.nio.ByteBuffer;

import mks.myworkspace.cvhub.model.OrganizationJDBC;

@Component
public class OrganizationDao {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public OrganizationJDBC save(OrganizationJDBC organization) {
    	String sql = "INSERT INTO cvhub_organization (title, logo_id, logo, website, summary, detail, " +
                "location, created_dte, modified_dte) VALUES (?, UUID_TO_BIN(?), ?, ?, ?, ?, ?, ?, ?)";
                    
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, organization.getTitle());
            ps.setString(2, organization.getLogoID() != null ? organization.getLogoID().toString() : null);
            ps.setBytes(3, organization.getLogo());    // byte array
            ps.setString(4, organization.getWebsite());
            ps.setString(5, organization.getSummary());
            ps.setString(6, organization.getDetail());
            ps.setString(7, organization.getLocation());
            ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
            return ps;
        }, keyHolder);
        
        organization.setId(keyHolder.getKey().longValue());
        return organization;
    }
    
    public OrganizationJDBC findById(Long id) {
        String sql = "SELECT id, title, BIN_TO_UUID(logo_id) as logo_id, logo, website, " +
                    "summary, detail, location, created_dte, modified_dte " +
                    "FROM cvhub_organization WHERE id = ?";
        
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                OrganizationJDBC org = new OrganizationJDBC();
                org.setId(rs.getLong("id"));
                org.setTitle(rs.getString("title"));
                String uuidStr = rs.getString("logo_id");
                if (uuidStr != null) {
                    org.setLogoID(UUID.fromString(uuidStr));
                }
                org.setLogo(rs.getBytes("logo"));
                org.setWebsite(rs.getString("website"));
                org.setSummary(rs.getString("summary"));
                org.setDetail(rs.getString("detail"));
                org.setLocation(rs.getString("location"));
                org.setCreatedDate(rs.getTimestamp("created_dte"));
                org.setModifiedDate(rs.getTimestamp("modified_dte"));
                return org;
            }, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    public void updateuser(OrganizationJDBC organization) {
        String sql = "UPDATE cvhub_organization SET user_id = ?, modified_dte = ? WHERE id = ?";
        
        jdbcTemplate.update(sql,
            organization.getUserId(),
            new Timestamp(System.currentTimeMillis()),
            organization.getId()
        );
    }
    
    public void update(OrganizationJDBC organization) {
        String sql = "UPDATE cvhub_organization SET title = ?, logo_id = UUID_TO_BIN(?), " +
                     "logo = ?, website = ?, summary = ?, detail = ?, location = ?, " +
                     "modified_dte = ? WHERE id = ?";
        
        jdbcTemplate.update(sql,
            organization.getTitle(),
            organization.getLogoID() != null ? organization.getLogoID().toString() : null,
            organization.getLogo(),
            organization.getWebsite(),
            organization.getSummary(),
            organization.getDetail(),
            organization.getLocation(),
            new Timestamp(System.currentTimeMillis()),
            organization.getId()
        );
    }
}