package mks.myworkspace.cvhub.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class JobRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Business fields here
    
    @CreationTimestamp
    @Column(name="created_dte")
    Date createdDate;
    
    @Column(name="modified_dte")
    @UpdateTimestamp
    Date modified;
}
