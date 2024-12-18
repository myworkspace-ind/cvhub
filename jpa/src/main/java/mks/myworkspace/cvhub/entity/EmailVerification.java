package mks.myworkspace.cvhub.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class EmailVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String email;
    private String verificationCode;
    private String status; // Trạng thái mã xác nhận (pending/verified)
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    public EmailVerification(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }

    public EmailVerification() {
    }
    // Getter and Setter
}