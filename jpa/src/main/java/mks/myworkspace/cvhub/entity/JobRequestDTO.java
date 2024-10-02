package mks.myworkspace.cvhub.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JobRequestDTO {
	private String title;
    private int locationCode;
    private int districtCode;
    private Long jobRoleId;
    private Integer experience;
    private Integer salary;
    private Long organizationId;
    private String jobDescription;
}