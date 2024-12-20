package mks.myworkspace.cvhub.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
    private String email;
    private String website;
    private byte[] logo;
    private Long userId;
    private Date createdDate;
    private Date modifiedDate;
    private String status;

    @Override
    public String toString() {
        return "Organization{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", email='" + email + '\'' +
               ", phone='" + phone + '\'' +
               ", status='" + status + '\'' +
               '}';
    }
}