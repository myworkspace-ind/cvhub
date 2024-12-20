package mks.myworkspace.cvhub.service.impl;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class OTPService {

    private static final int OTP_LENGTH = 6;  // Độ dài OTP, bạn có thể thay đổi

    public String generateOTP() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));  // Thêm số ngẫu nhiên từ 0 đến 9
        }
        return otp.toString();
    }
}