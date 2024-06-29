package com.jobportal.jobportal.users.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data 
@Builder
@AllArgsConstructor
@Document(collection = "users")
public class User {
    private String firstName;
    private String lastName;
    private String gender;

    @Indexed(unique = true)
    private String email;
    
    @Indexed(unique = true)
    private String phoneNumber;
    private String password;
    private String avatarString;
}
