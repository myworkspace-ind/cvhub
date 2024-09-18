package mks.myworkspace.cvhub.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JobSearchDTO {
    private String keyword;
    private int location;
    private Long industry;
}