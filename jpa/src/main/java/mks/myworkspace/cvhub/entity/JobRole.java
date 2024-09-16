package mks.myworkspace.cvhub.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name="cvhub_jobrole", uniqueConstraints=@UniqueConstraint(columnNames = "id"))
public class JobRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToOne // Mối quan hệ nhiều-một
    @JoinColumn(name = "cvhub_location")
    private Location location;
    private String industry;
    private Integer experience;
    private Integer salary;
	public JobRole(Long id, String title, Location location, String industry,Integer experience,Integer salary) {
		super();
		this.id = id;
		this.title = title;
		this.location = location;
		this.industry = industry;
		this.experience=experience;
		this.salary=salary;
		
		
	}

	public JobRole() {
		super();
		// TODO Auto-generated constructor stub
	}


	@CreationTimestamp
    @Column(name="created_dte",updatable = false)
    Date createdDate;
    
    @Column(name="modified_dte")
    @UpdateTimestamp
    Date modified;
}
