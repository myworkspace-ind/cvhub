package mks.myworkspace.cvhub.service;

import org.springframework.stereotype.Service;

import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.repository.UserRepository;

@Service
public interface UserService {
	UserRepository getRepo();
	User createUser(String fullName, String email, String password,String rePassword, String phone) throws Exception;
	boolean isEmailExists(String email);
}