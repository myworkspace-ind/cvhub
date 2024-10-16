package mks.myworkspace.cvhub.controller.model;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class OrganizationDTO {
	private String title;
    private MultipartFile logoFile;
    private String logoUrl;
    private String website;
    private String summary;
    private String detail;
    private String location;
    
}

