package mks.myworkspace.cvhub.controller.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CvDTO {
    private String fullName;
    private Long jobRoleId;
    private int locationCode;
    private String email;
    private String phone;
    private String education;
    private String skills;
    private String experience;
    private String projects;
    private String certifications;
    private String activities;
    private MultipartFile logoFile;
}