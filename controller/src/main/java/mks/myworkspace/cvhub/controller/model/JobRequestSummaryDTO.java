package mks.myworkspace.cvhub.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobRequestSummaryDTO {
	private Long id;
	private String title;
	private String location;
	private Integer experience;
	private Integer salary;
}
