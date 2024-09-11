package mks.myworkspace.cvhub.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="cvhub_location", uniqueConstraints=@UniqueConstraint(columnNames = "code"))
public class Location {
	@Id
    private int code;
    private String name;
    private String division_type;
	public Location(String name, int code, String division_type) {
		super();
		this.name = name;
		this.code = code;
		this.division_type = division_type;
	}
	
	public Location() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDivision_type() {
		return division_type;
	}
	public void setDivision_type(String division_type) {
		this.division_type = division_type;
	}
    
}