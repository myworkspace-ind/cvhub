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
}