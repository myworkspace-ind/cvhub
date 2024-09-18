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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="cvhub_jobrequest", uniqueConstraints=@UniqueConstraint(columnNames = "id"))
public class JobRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;
    @ManyToOne
    @JoinColumn(name = "jobrole_id")
    private JobRole jobRole;
    private Integer experience;
    private Integer salary;
    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;
	


	public JobRequest(Long id, String title, Location location, JobRole jobRole, Integer experience, Integer salary,
			Organization organization) {
		super();
		this.id = id;
		this.title = title;
		this.location = location;
		this.jobRole = jobRole;
		this.experience = experience;
		this.salary = salary;
		this.organization = organization;
	}



	@CreationTimestamp
    @Column(name="created_dte",updatable = false)
    Date createdDate;
    
    @Column(name="modified_dte")
    @UpdateTimestamp
    Date modified;
}