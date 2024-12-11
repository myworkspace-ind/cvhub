package mks.myworkspace.cvhub.service.impl;

import java.net.PasswordAuthentication;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import mks.myworkspace.cvhub.entity.EmailVerification;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.repository.EmailVerificationRepository;
import mks.myworkspace.cvhub.repository.UserRepository;
import mks.myworkspace.cvhub.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	@Value("${smtp@org.sakaiproject.email.api.EmailService}")
	private String smtpHost;

	@Value("${smtpPort@org.sakaiproject.email.api.EmailService}")
	private int smtpPort;

	@Value("${smtpUser@org.sakaiproject.email.api.EmailService}")
	private String smtpUsername;

	@Value("${smtpPassword@org.sakaiproject.email.api.EmailService}")
	private String smtpPassword;

	@Value("${smtpUseSSL@org.sakaiproject.email.api.EmailService}")
	private boolean smtpUseSSL;

	@Value("${smtpFrom@org.sakaiproject.email.api.EmailService}")
	private String smtpFrom;

	@Value("${smtpDebug@org.sakaiproject.email.api.EmailService}")
	private boolean smtpDebug;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Override
    public void sendEmail(String to, String subject, String body) throws MessagingException {
        // Cấu hình SMTP server
        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", smtpUseSSL); // Dùng `STARTTLS` nếu `smtpUseSSL` là true
        if (smtpDebug) {
            properties.put("mail.debug", "true");
        }

        // Tạo session với xác thực
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(smtpUsername, smtpPassword);
            }
        });

        // Tạo thông điệp email
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(smtpFrom)); // Gửi từ địa chỉ định nghĩa trong `smtpFrom`
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(body);

        // Gửi email
        Transport.send(message);
    }


    @Override
    public boolean verifyEmail(String email, String code) throws Exception {
        // Kiểm tra mã xác nhận trong bảng email_verification
        EmailVerification verification = emailVerificationRepository.findByEmail(email);
        if (verification != null && verification.getVerificationCode().equals(code)) {
            // Cập nhật trạng thái người dùng
            User user = userRepository.findUserByEmail(email);
            if (user != null) {
                user.setStatus("accept");  // Cập nhật trạng thái người dùng thành accept
                userRepository.save(user);

                // Đánh dấu mã xác nhận đã được sử dụng
                verification.setStatus("verified");
                emailVerificationRepository.save(verification);

                return true;  // Thành công
            }
        }
        return false;  // Nếu không tìm thấy email hoặc mã không khớp
    }
    
}