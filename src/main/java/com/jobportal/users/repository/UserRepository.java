package com.jobportal.users.repository;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.jobportal.users.model.User;

public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByEmail(String email);
    
    boolean existsByPhoneNumber(String phoneNumber);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByPhoneNumber(String phoneNumber);
}
