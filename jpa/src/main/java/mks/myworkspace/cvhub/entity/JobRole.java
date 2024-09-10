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
	public JobRole(Long id, String title, Location location, String industry) {
		super();
		this.id = id;
		this.title = title;
		this.location = location;
		this.industry = industry;
	}
    // Business fields here
    
    @CreationTimestamp
    @Column(name="created_dte")
    Date createdDate;
    
    @Column(name="modified_dte")
    @UpdateTimestamp
    Date modified;
}
