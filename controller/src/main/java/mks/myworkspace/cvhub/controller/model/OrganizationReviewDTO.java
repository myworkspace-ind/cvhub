package mks.myworkspace.cvhub.controller.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrganizationReviewDTO {
	private Integer rating;
	private String reviewText;
	private Date createdDate;
	private Date modifiedDate;
}
