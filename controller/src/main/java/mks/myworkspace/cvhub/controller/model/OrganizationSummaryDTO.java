package mks.myworkspace.cvhub.controller.model;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationSummaryDTO {
	private Long id;
	private UUID logoID;
	private byte[] logo;
    private String website;
    private String title;
    private String detail;
    private String location;
    private Date createdDate;
    private Long jobCount;
    private Long applicantCount;
}
