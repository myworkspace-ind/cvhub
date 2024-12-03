package mks.myworkspace.cvhub.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.repository.UserRepository;
import mks.myworkspace.cvhub.service.UserService;
@Service
public class UserImpl implements UserService {
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Getter
	@Autowired
	UserRepository repo;
//	 @Autowired
//	private PasswordEncoder passwordEncoder;
	@Override
	@Transactional
	public User createUser(String fullName, String email, String password, String phone) throws Exception {
	    if (isEmailExists(email)) {
	        throw new Exception("Email already exists");
	    }

	    String sqlInsert = "INSERT INTO cvhub_user (fullname, email, password, phone, role, created_date, modified_date) " +
	                       "VALUES (?, ?, ?, ?, ?, ?, ?)";
	    Date currentDate = new Date();
	    jdbcTemplate.update(sqlInsert, fullName, email, password, phone, "ROLE_USER", currentDate, currentDate);

	    String sqlSelect = "SELECT * FROM cvhub_user WHERE email = ?";
	    return jdbcTemplate.queryForObject(sqlSelect, new BeanPropertyRowMapper<>(User.class), email);
	}


	@Override
	public boolean isEmailExists(String email) {
		return repo.existsByEmail(email);
	}

	@Override
	public User findUserByEmail(String email) {
		return repo.findUserByEmail(email);
	}

	@Override
	public User getUser(Long id) {
		// TODO Auto-generated method stub
		return repo.findById(id).get();
	}
	
	@Override
    public Page<User> findUsersByPeriod(String period, Pageable pageable) {
        Date startOfPeriod = calculateStartOfPeriod(period);
        return repo.findUsersByCreationDateWithRoleUser(startOfPeriod, pageable);
    }

    private Date calculateStartOfPeriod(String period) {
        Calendar calendar = Calendar.getInstance();

        switch (period.toLowerCase()) {
            case "today":
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                break;

            case "week":
            	// Mặc định sẽ lấy ngày Chủ nhật là ngày đầu tiên nên phải cấu hình lại
            	calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                break;

            case "month":
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                break;

            case "year":
                calendar.set(Calendar.DAY_OF_YEAR, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                break;

            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }

        return calendar.getTime();
    }
}