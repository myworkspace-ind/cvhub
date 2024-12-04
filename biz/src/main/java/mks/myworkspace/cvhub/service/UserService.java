package mks.myworkspace.cvhub.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.repository.UserRepository;

@Service
public interface UserService {
	UserRepository getRepo();
	User createUser(String fullName, String email, String password, String phone) throws Exception;
	boolean isEmailExists(String email);
	User findUserByEmail(String email);
	User getUser(Long id);
	void registerUserInSakai(String fullName, String email, String password, String phone) throws Exception;
	Page<User> findUsersByPeriod(String period, Pageable pageable);
}