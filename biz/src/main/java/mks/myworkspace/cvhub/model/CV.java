package mks.myworkspace.cvhub.model;

import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CV {
    private Long id;
    private String fullName;
    private Long userId;      // Thay thế cho ManyToOne User
    private Long jobRoleId;   // Thay thế cho ManyToOne JobRole
    private String email;
    private String phone;
    private Long locationId;  // Thay thế cho ManyToOne Location
    private String education;
    private String skills;
    private String experience;
    private String projects;
    private String certifications;
    private String activities;
    private UUID logoID;
    private byte[] logo;
    private boolean isprimary;
    private boolean selected;
    private Date createdDate;
    private Date modifiedDate;

    // Constructor với các trường bắt buộc
    public CV(String fullName, Long jobRoleId, String email, String phone, 
              Long locationId, String education, String skills, String experience, 
              String projects, String certifications, String activities, 
              UUID logoID, byte[] logo) {
        this.fullName = fullName;
        this.jobRoleId = jobRoleId;
        this.email = email;
        this.phone = phone;
        this.locationId = locationId;
        this.education = education;
        this.skills = skills;
        this.experience = experience;
        this.projects = projects;
        this.certifications = certifications;
        this.activities = activities;
        this.logoID = logoID;
        this.logo = logo;
        this.isprimary = false;
        this.selected = false;
    }

    @Override
    public String toString() {
        return "CV{" +
               "id=" + id +
               ", fullName='" + fullName + '\'' +
               ", userId=" + userId +
               ", jobRoleId=" + jobRoleId +
               ", email='" + email + '\'' +
               ", phone='" + phone + '\'' +
               ", locationId=" + locationId +
               ", isPrimary=" + isprimary +
               ", selected=" + selected +
               '}';
    }
}