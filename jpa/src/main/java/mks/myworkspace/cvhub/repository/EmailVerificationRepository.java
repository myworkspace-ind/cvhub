package mks.myworkspace.cvhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mks.myworkspace.cvhub.entity.EmailVerification;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, String> {

    // Tìm EmailVerification bằng email
    EmailVerification findByEmail(String email);

    // Kiểm tra xem email đã được xác nhận chưa
    boolean existsByEmailAndStatus(String email, String status);
}
