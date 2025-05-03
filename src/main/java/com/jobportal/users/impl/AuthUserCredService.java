package com.jobportal.users.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jobportal.users.model.User;
import com.jobportal.users.repository.UserRepository;

@Service
public class AuthUserCredService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user;

        user = userRepository.findByEmail(username);
        if (user.get() == null) {
            user = userRepository.findByPhoneNumber(username);

            if (user.get() == null) {
                throw new UsernameNotFoundException("Invalid user");
            }
        }

        return new org.springframework.security.core.userdetails.User(
                user.get().getEmail(),
                user.get().getPasswordHash(), 
                List.of()
            );
    }
}
