package com.jobportal.user.service;

import com.jobportal.user.dto.CreateUserRequest;
import com.jobportal.user.dto.ValidateUser;
import com.jobportal.user.exception.UserException;
import com.jobportal.user.model.User;
import com.jobportal.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;

    /**
     * Creates a new user profile with secure credentials and associated media.
     * <p>
     * This operation is <strong>@Transactional</strong>; any failure in database
     * persistence or image storage will trigger a complete rollback.
     * </p>
     *
     * @param  userObj  Request DTO containing personal and security credentials.
     * @param  images   List of profile images to be processed by {@link StorageService}.
     * @return The persisted {@link User} entity with its generated ID.
     * @throws UserException.UserCreationFailed if database integrity is violated.
     * @throws UserException.ImagesNotSent      if the image list is empty or null.
     */
    @Transactional
    public User createUser(CreateUserRequest userObj, List<MultipartFile> images) {
        validateUser(new ValidateUser(userObj.getEmail(), userObj.getPhoneNumber()));

        User user = User.builder()
                .userName(userObj.getName())
                .userLastName(userObj.getLastName())
                .emailId(userObj.getEmail())
                .phoneNo(userObj.getPhoneNumber())
                .userDOB(userObj.getDob())
                .gender(userObj.getGender())
                .password(userObj.getPassword())
                .passwordHash(passwordEncoder.encode(userObj.getPassword()))
                .build();

        try {
            user = userRepository.save(user);
        } catch(RuntimeException ex) {
            throw new UserException.UserCreationFailed(ex);
        }

        if(!images.isEmpty()) {
            storageService.saveImages(images, user.getUserId().toString());
        } else {
            throw new UserException.ImagesNotSent();
        }

        return user;
    }

    /**
     * Performs pre-registration validation to ensure data uniqueness.
     * <p>
     * This method verifies that the provided contact identifiers (Email and Phone Number)
     * are not already associated with an existing account in the {@link UserRepository}.
     * </p>
     *
     * <strong>Validation Logic:</strong>
     * <ul>
     *   <li>Checks for duplicate <strong>Email Address</strong>.</li>
     *   <li>Checks for duplicate <strong>Phone Number</strong>.</li>
     * </ul>
     *
     * @param userObj a {@link ValidateUser} object containing the credentials to be verified.
     * @throws UserException.EmailAlreadyExist if the email is already registered in the system.
     * @throws UserException.PhoneNumberAlreadyExist if the phone number is already registered in the system.
     */
    public void validateUser(ValidateUser userObj) {
        if(userRepository.existsByEmailId(userObj.getEmail())) {
            throw new UserException.EmailAlreadyExist(userObj.getEmail());
        }

        if(userRepository.existsByPhoneNo(userObj.getPhoneNumber())) {
            throw new UserException.PhoneNumberAlreadyExist(userObj.getPhoneNumber());
        }
    }

    /**
     * Retrieves a user profile based on a flexible identifier (Email or Phone).
     * <p>
     * This method automatically detects the input type:
     * <ul>
     *   <li>If the identifier contains an {@code '@'} symbol, it performs a lookup by <strong>Email</strong>.</li>
     *   <li>Otherwise, it defaults to a lookup by <strong>Phone Number</strong>.</li>
     * </ul>
     * </p>
     *
     * @param emailOrPhoneNo a {@link String} containing the user's primary contact identifier.
     * @return the {@link User} entity associated with the provided identifier.
     * @throws UserException.UserDoesNotExist if no account matches the given credentials.
     */
    public User getUser(String emailOrPhoneNo) {
        Optional<User> user;

        if(emailOrPhoneNo.contains("@")) {
            user = userRepository.findByEmailId(emailOrPhoneNo);
        } else {
            user = userRepository.findByPhoneNo(emailOrPhoneNo);
        }

        return user.orElseThrow(UserException.UserDoesNotExist::new);
    }
}
