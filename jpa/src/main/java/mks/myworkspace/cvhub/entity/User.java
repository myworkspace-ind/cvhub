package mks.myworkspace.cvhub.entity;

import java.util.Date;
import java.util.List;
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
@Table(name = "cvhub_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String fullName;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    private String phone;
    

    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CV> cvList;
    
    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private Date createdDate;
    
    @UpdateTimestamp
    @Column(name = "modified_date")
    private Date modifiedDate;
}