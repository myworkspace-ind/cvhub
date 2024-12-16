package mks.myworkspace.cvhub.service.impl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomCasUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Trong trường hợp này, username sẽ là tên người dùng nhận được từ CAS
        // Bạn có thể tùy chỉnh để lấy các thông tin người dùng khác từ CAS hoặc cơ sở dữ liệu.
        // Ví dụ, tạo một User với username và quyền hạn giả định.

        if (username == null || username.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        // Tạo một UserDetails (nếu cần, có thể kết nối với cơ sở dữ liệu để lấy thông tin người dùng)
        return User.withUsername(username)
                   .password("")  // CAS không sử dụng mật khẩu thông thường, nên để trống
                   .roles("USER")  // Ví dụ, chỉ định quyền cho người dùng
                   .build();
	}
}
