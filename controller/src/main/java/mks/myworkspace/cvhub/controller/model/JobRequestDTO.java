package mks.myworkspace.cvhub.controller.model;


import java.time.LocalDate;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mks.myworkspace.cvhub.entity.Organization;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobRequestDTO {
	private String title;
    private int locationCode;
    private Long jobRoleId;
    private Integer experience;
    private Integer salary;
    private Long organizationId;
    private String jobDescription;
    private String requirementsCandidate;
    private String benefitCandidate;
    private Organization organization;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate deadlineApplication;
}