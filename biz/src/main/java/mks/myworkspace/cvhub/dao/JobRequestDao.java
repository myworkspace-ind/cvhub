package mks.myworkspace.cvhub.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import mks.myworkspace.cvhub.model.JobRequestJDBC;

@Component
public class JobRequestDao {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public JobRequestJDBC save(JobRequestJDBC jobRequest) {
        String sql = "INSERT INTO cvhub_jobrequest (title, location_id, jobrole_id, experience, salary, " +
                    "organization_id, detailsJob, requirementsCandidate, benefitCandidate, " +
                    "deadlineApplication, created_dte, modified_dte) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, jobRequest.getTitle());
            ps.setLong(2, jobRequest.getLocationId());
            ps.setObject(3, jobRequest.getJobRoleId());  // Có thể null
            ps.setObject(4, jobRequest.getExperience()); // Có thể null
            ps.setObject(5, jobRequest.getSalary());     // Có thể null
            ps.setLong(6, jobRequest.getOrganizationId());
            ps.setString(7, jobRequest.getDetailsJob());
            ps.setString(8, jobRequest.getRequirementsCandidate());
            ps.setString(9, jobRequest.getBenefitCandidate());
            ps.setObject(10, jobRequest.getDeadlineApplication());
            ps.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
            ps.setTimestamp(12, new Timestamp(System.currentTimeMillis()));
            return ps;
        }, keyHolder);
        
        jobRequest.setId(keyHolder.getKey().longValue());
        return jobRequest;
    }
    
    public JobRequestJDBC findById(Long id) {
        String sql = "SELECT jr.*, l.name as location_name, jr.title as job_role_title, o.title as organization_name " +
                    "FROM cvhub_jobrequest jr " +
                    "LEFT JOIN cvhub_location l ON jr.location_id = l.code " +
                    "LEFT JOIN cvhub_organization o ON jr.organization_id = o.id " +
                    "WHERE jr.id = ?";
                    
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                JobRequestJDBC jobRequest = new JobRequestJDBC();
                jobRequest.setId(rs.getLong("id"));
                jobRequest.setTitle(rs.getString("title"));
                jobRequest.setLocationId(rs.getLong("location_id"));
                jobRequest.setJobRoleId(rs.getObject("jobrole_id") != null ? rs.getLong("jobrole_id") : null);
                jobRequest.setExperience(rs.getObject("experience") != null ? rs.getInt("experience") : null);
                jobRequest.setSalary(rs.getObject("salary") != null ? rs.getInt("salary") : null);
                jobRequest.setDetailsJob(rs.getString("detailsJob"));
                jobRequest.setRequirementsCandidate(rs.getString("requirementsCandidate"));
                jobRequest.setBenefitCandidate(rs.getString("benefitCandidate"));
                jobRequest.setOrganizationId(rs.getLong("organization_id"));
                jobRequest.setDeadlineApplication(rs.getObject("deadlineApplication") != null ? 
                    rs.getDate("deadlineApplication").toLocalDate() : null);
                jobRequest.setCreatedDate(rs.getTimestamp("created_dte"));
                jobRequest.setModifiedDate(rs.getTimestamp("modified_dte"));
                
                // Thông tin phụ
                jobRequest.setLocationName(rs.getString("location_name"));
                jobRequest.setJobRoleTitle(rs.getString("job_role_title"));
                jobRequest.setOrganizationName(rs.getString("organization_name"));
                
                return jobRequest;
            }, id);
        } catch (Exception e) {
            return null;
        }
    }
    
    public void update(JobRequestJDBC jobRequest) {
        String sql = "UPDATE cvhub_jobrequest SET title = ?, location_id = ?, jobrole_id = ?, " +
                     "experience = ?, salary = ?, detailsJob = ?, requirementsCandidate = ?, " +
                     "benefitCandidate = ?, deadlineApplication = ?, modified_dte = ? " +
                     "WHERE id = ?";
                     
        jdbcTemplate.update(sql,
            jobRequest.getTitle(),
            jobRequest.getLocationId(),
            jobRequest.getJobRoleId(),
            jobRequest.getExperience(),
            jobRequest.getSalary(),
            jobRequest.getDetailsJob(),
            jobRequest.getRequirementsCandidate(),
            jobRequest.getBenefitCandidate(),
            jobRequest.getDeadlineApplication(),
            new java.sql.Timestamp(System.currentTimeMillis()),
            jobRequest.getId()
        );
    }
    
    public void delete(Long id) {
        String sql = "DELETE FROM cvhub_jobrequest WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}