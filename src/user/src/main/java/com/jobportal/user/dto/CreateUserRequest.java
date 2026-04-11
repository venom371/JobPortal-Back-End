package com.jobportal.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest  {
    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private char gender;
    private LocalDate dob;
}
