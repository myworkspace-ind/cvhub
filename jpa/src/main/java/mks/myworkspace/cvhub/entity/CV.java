package mks.myworkspace.cvhub.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import javax.persistence.*;
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
@Table(name="cvhub_cv", uniqueConstraints=@UniqueConstraint(columnNames = "id"))
public class CV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "jobrole_id")
    private JobRole jobRole;
    
    private String email;
    
    private String phone;
    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;
    @Column(columnDefinition = "TEXT")
    private String education;
    
    @Column(columnDefinition = "TEXT")
    private String skills;
    
    @Column(columnDefinition = "TEXT")
    private String experience;
    
    @Column(columnDefinition = "TEXT")
    private String projects;
    
    @Column(columnDefinition = "TEXT")
    private String certifications;
    
    @Column(columnDefinition = "TEXT")
    private String activities;
    
    @Column(name = "logo_id", columnDefinition = "BINARY(16)")
	private UUID logoID;
	@Lob
	private byte[] logo;

    @CreationTimestamp
    @Column(name="created_date", updatable = false)
    private Date createdDate;
    
    @UpdateTimestamp
    @Column(name="modified_date")
    private Date modifiedDate;

	public CV(String fullName, JobRole jobRole, String email, String phone, Location location, String education,
			String skills, String experience, String projects, String certifications, String activities, UUID logoID,
			byte[] logo) {
		super();
		this.fullName = fullName;
		this.jobRole = jobRole;
		this.email = email;
		this.phone = phone;
		this.location = location;
		this.education = education;
		this.skills = skills;
		this.experience = experience;
		this.projects = projects;
		this.certifications = certifications;
		this.activities = activities;
		this.logoID = logoID;
		this.logo = logo;
	}

	




}