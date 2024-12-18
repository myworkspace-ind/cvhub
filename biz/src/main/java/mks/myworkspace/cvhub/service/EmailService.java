package mks.myworkspace.cvhub.service;

public interface EmailService {
	 void sendEmail(String to, String subject, String body) throws Exception;

	    boolean verifyEmail(String email, String code) throws Exception;
}
