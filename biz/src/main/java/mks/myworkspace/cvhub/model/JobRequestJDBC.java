package mks.myworkspace.cvhub.model;

import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobRequestJDBC {
    private Long id;
    private String title;
    private Long locationId;  
    private Long jobRoleId;
    private Integer experience;
    private Integer salary;
    private String detailsJob;
    private String requirementsCandidate;
    private String benefitCandidate;
    private Long organizationId;
    private LocalDate deadlineApplication;
    private Date createdDate;
    private Date modifiedDate;
    
    private String locationName;      // Tên location
    private String jobRoleTitle;      // Tên job role
    private String organizationName;  // Tên organization
}