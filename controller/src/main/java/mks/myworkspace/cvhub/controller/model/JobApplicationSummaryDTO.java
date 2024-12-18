package mks.myworkspace.cvhub.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationSummaryDTO {
	private Long jobId;
	private String jobTitle;
	private String fullName;
	private String email;
	private String phone;
}
