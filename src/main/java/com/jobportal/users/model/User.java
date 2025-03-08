package com.jobportal.users.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data 
@Builder
@AllArgsConstructor
@Document(collection = "Users")
public class User {
    @Id
    @Field("_id")
    private String id;
    private String firstName;
    private String lastName;
    private String gender;
    private long dob;
    @Indexed(unique = true)
    private String email;
    @Indexed(unique = true)
    private String phoneNumber;
    private String password;
}
