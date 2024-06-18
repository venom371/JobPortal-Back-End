package com.jobportal.jobportal.users.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jobportal.jobportal.users.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    
}
