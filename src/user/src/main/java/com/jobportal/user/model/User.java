package com.jobportal.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Setter @Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @Column(name = "user_id", columnDefinition = "Binary(16)", nullable = false, updatable = false, unique = true)
    private UUID userId;

    @Column(name = "name", length = 100, nullable = false)
    private String userName;

    @Column(name = "last_name", length = 100, nullable = false)
    private String userLastName;

    @Column(name = "gender", length = 1, nullable = false)
    private char gender;

    @Column(name = "phonenumber", length = 15, nullable = false)
    private String phoneNo;

    @Column(name = "email_id", length = 254, nullable = false)
    private String emailId;

    @Column(name = "dob", nullable = false)
    private LocalDate userDOB;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false, updatable = false)
    private Instant userCreationDate;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant userLastModifiedAt;

    @Column(name = "password", length = 30, nullable = false)
    private String password;

    @Column(name = "password_hash", length = 60, nullable = false)
    private String passwordHash;

    @PrePersist
    public void generateUserId(){
        if(this.userId == null)
            this.userId = UUID.randomUUID();
    }
}
