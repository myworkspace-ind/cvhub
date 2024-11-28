package mks.myworkspace.cvhub.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class EncodePasswordImpl implements PasswordEncoder {
	private static final String PBKDF2 = "PBKDF2:";
	private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
	private static final int ITERATIONS = 8192;
	private static final int KEY_LENGTH = 128;
	private static final int SALT_LENGTH = 16;
	private static final String SALT_DELIMITER = ":";

	private final SecureRandom secureRandom = new SecureRandom();

	@Override
	public String encode(CharSequence rawPassword) {
	    // Tạo salt ngẫu nhiên
	    byte[] salt = generateSalt(SALT_LENGTH);

	    // Tạo hash mật khẩu dựa trên mật khẩu thô và salt
	    byte[] hash = hashPassword(rawPassword.toString(), salt, ITERATIONS, KEY_LENGTH);

	    // Ghép chuỗi theo định dạng: PBKDF2:salt:hash
	    return PBKDF2 +
	           Base64.getEncoder().encodeToString(salt) +
	           SALT_DELIMITER +
	           Base64.getEncoder().encodeToString(hash);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
	    // Loại bỏ tiền tố "PBKDF2:" nếu có
	    if (encodedPassword.startsWith(PBKDF2)) {
	        encodedPassword = encodedPassword.substring(PBKDF2.length());
	    }

	    // Tách salt và hash từ chuỗi mã hóa
	    String[] parts = encodedPassword.split(SALT_DELIMITER);
	    if (parts.length != 2) return false;

	    // Giải mã salt và hash từ base64
	    byte[] salt = Base64.getDecoder().decode(parts[0]);
	    byte[] expectedHash = Base64.getDecoder().decode(parts[1]);

	    // Tạo hash từ mật khẩu thô và salt
	    byte[] actualHash = hashPassword(rawPassword.toString(), salt, ITERATIONS, KEY_LENGTH);

	    // So sánh hash để kiểm tra mật khẩu
	    return MessageDigest.isEqual(expectedHash, actualHash);
	}

	private byte[] generateSalt(int length) {
	    byte[] salt = new byte[length];
	    secureRandom.nextBytes(salt);
	    return salt;
	}

	private byte[] hashPassword(String password, byte[] salt, int iterations, int keyLength) {
	    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
	    try {
	        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
	        return factory.generateSecret(spec).getEncoded();
	    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
	        throw new RuntimeException("Error while hashing password", e);
	    }
	}

    // Phương thức main để kiểm tra mã hóa và so sánh mật khẩu
    public static void main(String[] args) {
        EncodePasswordImpl passwordEncoder = new EncodePasswordImpl();

        String password = "123";
        System.out.println("Original password: " + password);

        // Mã hóa mật khẩu
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println("Encoded password: " + encodedPassword);

        // Kiểm tra nếu mật khẩu nhập vào khớp với mật khẩu đã mã hóa
        boolean isMatch = passwordEncoder.matches(password, encodedPassword);
        System.out.println("Password match: " + isMatch);

        // Thử với mật khẩu không khớp
        boolean isMatchFalse = passwordEncoder.matches("wrongPassword", encodedPassword);
        System.out.println("Password match (incorrect password): " + isMatchFalse);
    }
}
