package mks.myworkspace.cvhub.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobRoleJDBC {
    private Long id;
    private String title;
    private String description;
    private Date createdDate;
    private Date modifiedDate;

    public JobRoleJDBC(String title, String description) {
        this.title = title;
        this.description = description;
    }
}