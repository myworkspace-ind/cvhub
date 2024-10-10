package mks.myworkspace.cvhub.controller.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JobRequestDTO {
	private String title;
    private int locationCode;
    private Long jobRoleId;
    private Integer experience;
    private Integer salary;
    private Long organizationId;
    private String jobDescription;
}