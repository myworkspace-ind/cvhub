package mks.myworkspace.cvhub.entity;

import javax.persistence.Entity;
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
	    
}