package mks.myworkspace.cvhub.controller.model;

import java.io.Serializable;
import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cv implements Serializable{
	private MultipartFile attrachment;
}