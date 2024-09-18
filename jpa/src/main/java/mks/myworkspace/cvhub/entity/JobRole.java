package mks.myworkspace.cvhub.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = "cvhub_jobrole", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class JobRole {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String description;
	public JobRole(String title, String description) {
		super();
		this.title = title;
		this.description = description;
	}

	@CreationTimestamp
	@Column(name = "created_dte", updatable = false)
	Date createdDate;

	@Column(name = "modified_dte")
	@UpdateTimestamp
	Date modified;
}