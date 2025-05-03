package com.jobportal.users.beans;

import lombok.Data;

@Data
public class UserLogin {
    private String emailOrPhoneNumber;
    private String password;
}
