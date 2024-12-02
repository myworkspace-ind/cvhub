package mks.myworkspace.cvhub.controller.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JobSearch_tuan_DTO {
    private String keyword;
    private int location;
//    private Long industry;
    private String selectedIndustries;
    private String sort;
    private boolean bool; // true tim kiem theo ten job false tim kiem theo ten cong ty
    private int page;
    private int size;
}