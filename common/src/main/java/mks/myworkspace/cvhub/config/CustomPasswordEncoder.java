package mks.myworkspace.cvhub.config;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {
    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 8192;
    private static final int KEY_LENGTH = 128;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
    private final SecureRandom random = new SecureRandom();

    @Override
    public String encode(CharSequence rawPassword) {
        byte[] salt = generateSalt();
        byte[] hash = hashPassword(rawPassword.toString(), salt);
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    }

    @Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		 // Bỏ qua tiền tố "PBKDF2:" nếu có
	    if (encodedPassword.startsWith("PBKDF2:")) {
	        encodedPassword = encodedPassword.substring(7); // Loại bỏ tiền tố "PBKDF2:"
	    }

	    // Tách salt và hash
	    String[] parts = encodedPassword.split(":");
	    if (parts.length != 2) return false;

	    byte[] salt = Base64.getDecoder().decode(parts[0]);
	    byte[] expectedHash = Base64.getDecoder().decode(parts[1]);
	    byte[] actualHash = hashPassword(rawPassword.toString(), salt);

	    return MessageDigest.isEqual(expectedHash, actualHash);
	}

    private byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    private byte[] hashPassword(String password, byte[] salt) {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            return factory.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("Error while hashing password", e);
        }
    }
}
