package com.jobportal.auth.service;

import com.jobportal.auth.dto.LoginRequest;
import com.jobportal.user.exception.UserException;
import com.jobportal.user.model.User;
import com.jobportal.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JWTService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Authenticates a user and generates a secure JWT session cookie.
     * <p>
     * This method performs a multi-step authentication flow:
     * <ul>
     *   <li>Retrieves the user profile via the {@code userService}.</li>
     *   <li>Validates the provided credentials against the stored password hash.</li>
     *   <li>Generates a cryptographically signed JWT stored in a {@link ResponseCookie}.</li>
     * </ul>
     * </p>
     *
     * @param loginRequest a {@link LoginRequest} containing the user's identifier (email/phone) and raw password.
     * @return a {@link ResponseCookie} containing the generated JWT, configured with security flags.
     * @throws UserException.InvalidUserPassword if the password verification fails.
     * @throws UserException.UserDoesNotExist if the identifier cannot be found in the system.
     */
    public ResponseCookie loginUser(LoginRequest loginRequest) {
        log.info("Login attempt by user: {}", loginRequest.getEmailOrPhoneNo());
        User user = userService.getUser(loginRequest.getEmailOrPhoneNo());

        if(!passwordEncoder.matches((loginRequest.getPassword()), user.getPasswordHash())) {
            throw new UserException.InvalidUserPassword();
        }

        log.info("Login successful for user: {}", loginRequest.getEmailOrPhoneNo());

        return jwtService.createJWTCookie(user.getUserId(),user.getEmailId());
    }
}
