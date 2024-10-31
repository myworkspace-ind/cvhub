package mks.myworkspace.cvhub.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.repository.UserRepository;
import mks.myworkspace.cvhub.service.UserService;
@Service
public class UserImpl implements UserService {
	@Getter
	@Autowired
	UserRepository repo;
//	 @Autowired
//	private PasswordEncoder passwordEncoder;
	@Override
	@Transactional
	public User createUser(String fullName, String email, String password, String rePassword, String phone) throws Exception {
		if (isEmailExists(email)) {
            throw new Exception("Email already exists");
        }
        
        // Validate password match
        if (!password.equals(rePassword)) {
            throw new Exception("Passwords don't match");
        }
        
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
//        user.setPassword(passwordEncoder.encode(password));
        user.setPassword(password);
        user.setPhone(phone);
    
        return repo.save(user);
	}

	@Override
	public boolean isEmailExists(String email) {
		return repo.existsByEmail(email);
	}
}