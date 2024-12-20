package mks.myworkspace.cvhub.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserJDBC {
    private Long id;
    private String fullName;
    private byte[] image;
    private String email;
    private String password;
    private String phone;
    private String role;
    private Organization organization;  // Chỉ lưu reference
    private List<CV> cvList;           // Chỉ lưu reference
    private Date createdDate;
    private Date modifiedDate;
    private String status;

    // Các constructor tiện ích
    public UserJDBC(String fullName, String email, String password, String phone) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = "ROLE_USER";
    }

    // toString method cho logging và debugging
    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", fullName='" + fullName + '\'' +
               ", email='" + email + '\'' +
               ", phone='" + phone + '\'' +
               ", role='" + role + '\'' +
               ", status='" + status + '\'' +
               '}';
    }
}
