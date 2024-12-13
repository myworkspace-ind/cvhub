package mks.myworkspace.cvhub.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import mks.myworkspace.cvhub.entity.SakaiUser;
import mks.myworkspace.cvhub.entity.SakaiUserIdMap;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.repository.SakaiUserIdMapRepository;
import mks.myworkspace.cvhub.repository.SakaiUserRepository;
import mks.myworkspace.cvhub.repository.UserRepository;
import mks.myworkspace.cvhub.service.UserService;
@Service
public class UserImpl implements UserService {
	@Getter
	@Autowired
	UserRepository repo;
	@Autowired
    private SakaiUserRepository sakaiUserRepo;

    @Autowired
    private SakaiUserIdMapRepository sakaiUserIdMapRepo;
//	 @Autowired
//	private PasswordEncoder passwordEncoder;
	@Override
	@Transactional
	public User createUser(String fullName, String email, String password, String phone) throws Exception {
		if (isEmailExists(email)) {
            throw new Exception("Email already exists");
        }
        

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);

        user.setPassword(password);
        user.setPhone(phone);
        user.setRole("ROLE_USER");
        return repo.save(user);
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

	@Override
	@Transactional
	public void registerUserInSakai(String fullName, String email, String password, String phone) throws Exception {
		if (isEmailExists(email)) {
            throw new Exception("Email already exists");
        }

        // 2. Tạo userId
        String userId = generateUniqueId(); // Hàm tạo userId (tùy bạn triển khai)

        // 3. Lưu vào sakai_user
        SakaiUser sakaiUser = new SakaiUser();
        sakaiUser.setUserId(userId);
        sakaiUser.setEmail(email);
        sakaiUser.setEmailLc(email.toLowerCase());
        sakaiUser.setFirstName(extractFirstName(fullName));
        sakaiUser.setLastName(extractLastName(fullName));
        sakaiUser.setPassword(password); // Có thể mã hóa mật khẩu nếu cần
        sakaiUser.setPhone(phone);
        sakaiUser.setRole("ROLE_USER");
        sakaiUserRepo.save(sakaiUser);

        // 4. Lưu vào sakai_user_id_map
        SakaiUserIdMap sakaiUserIdMap = new SakaiUserIdMap();
        sakaiUserIdMap.setUserId(userId);
        sakaiUserIdMap.setEid(email); // Giả định dùng email làm EID
        sakaiUserIdMapRepo.save(sakaiUserIdMap);
	}
	private String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    // Hàm tách firstName từ fullName
    private String extractFirstName(String fullName) {
        String[] parts = fullName.split(" ");
        return parts.length > 1 ? parts[0] : fullName;
    }

    // Hàm tách lastName từ fullName
    private String extractLastName(String fullName) {
        String[] parts = fullName.split(" ");
        return parts.length > 1 ? parts[parts.length - 1] : "";
    }

	@Override
	public void deleteUserById(Long id) {
		repo.deleteById(id);
	}
	 @Override
	    public Long getUserCountPerMonth(int month, int year) {
	        // Đảm bảo rằng bạn gọi 'repo.getUserCountPerMonth()' thay vì 'userRepository.getUserCountPerMonth()'
	        return repo.getUserCountPerMonth(month, year);
	    }

	    @Override
	    public List<Long> getUserCountsPerYear(int year) {
	        List<Object[]> result = repo.getUserCountPerYear(year);
	        
	        // Chuyển kết quả trả về thành dạng mảng Long cho dễ xử lý
	        List<Long> userCounts = new ArrayList<>(Collections.nCopies(12, 0L));  // Mảng 12 tháng, mặc định là 0
	        for (Object[] row : result) {
	            Integer month = (Integer) row[0];  // Tháng
	            Long count = (Long) row[1];        // Số lượng người dùng
	            userCounts.set(month - 1, count);  // Cập nhật số lượng cho tháng tương ứng
	        }
	        return userCounts;
	    }


}