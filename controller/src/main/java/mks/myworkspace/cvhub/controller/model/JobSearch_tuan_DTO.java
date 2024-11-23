package mks.myworkspace.cvhub.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JobSearch_tuan_DTO {
    private String keyword;
    private int location;
    private Long industry;
    private String sort;
    private boolean bool; // true tim kiem theo ten job false tim kiem theo ten cong ty
}