package mks.myworkspace.cvhub.model;

import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationJDBC {
    private Long id;
    private String title;
    private UUID logoID;
    private byte[] logo;
    private String website;
    private String summary;
    private String detail;
    private String location;
    private Long userId;
    private Date createdDate;
    private Date modifiedDate;
    
    public OrganizationJDBC(String title, UUID logoID, byte[] logo, 
            String website, String summary, String detail, 
            String location, Long userId) {
        this.title = title;
        this.logoID = logoID;
        this.logo = logo;
        this.website = website;
        this.summary = summary;
        this.detail = detail;
        this.location = location;
        this.userId = userId;
    }
}