package mks.myworkspace.cvhub.controller.model;


import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private String fullName;
    private String email;
    private String password;
    private String confirmPassword;
    private String phone;
}