package com.tchat.api.services.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tchat.api.dto.requests.LoginRequestDto;
import com.tchat.api.dto.requests.RegisterRequestDto;
import com.tchat.api.dto.responses.AuthenticationResponse;
import com.tchat.api.entities.User;
import com.tchat.api.repositories.UserRepository;
import com.tchat.api.services.AuthenticationService;
import com.tchat.api.utilities.JwtUtil;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${dir.image.profile.upload}")
    private String uploadDir;

    @Override
    public AuthenticationResponse registerUser(RegisterRequestDto registerRequest) throws Exception {
        try {
            Optional<User> findUserByEmail = userRepository.findByEmail(registerRequest.getEmail());
            if (findUserByEmail.isPresent()) {
                throw new Exception("Email already exists!");
            }

            Optional<User> findUserByUsername = userRepository.findByUsername(registerRequest.getUsername());
            if (findUserByUsername.isPresent()) {
                throw new Exception("Username already exists!");
            }

            User user = User.builder()
                    .email(registerRequest.getEmail())
                    .fullName(registerRequest.getFullName())
                    .username(registerRequest.getUsername())
                    .password(registerRequest.getPassword())
                    .build();

            if (registerRequest.getImageProfile() != null) {
                String fileName = new Date().getTime() + "_" + StringUtils
                        .cleanPath(Objects.requireNonNull(registerRequest.getImageProfile().getOriginalFilename()));

                Path uploadPath = Path.of(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(registerRequest.getImageProfile().getInputStream(), filePath);
                user.setPathImageProfile(filePath.toString());
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreatedBy("SYSTEM");
            user.setUpdatedBy("SYSTEM");
            userRepository.save(user);

            UserDetails userDetails = loadUserByUsername(user.getUsername());
            String token = jwtUtil.generateToken(userDetails.getUsername());

            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setToken(token);

            return authenticationResponse;
        } catch (Exception e) {
            throw new Exception(e.getLocalizedMessage());
        }
    }

    @Override
    public AuthenticationResponse loginUser(LoginRequestDto request) {
        UserDetails userDetails = loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails.getUsername());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Password no valid!");
        }

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .token(token)
                .build();

        return authenticationResponse;
    }

    @Override
    public UserDetails loadUserByUsername(String request) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsernameOrEmail(request);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Username or Email are invalid!");
        }

        User user = userOptional.get();

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }

    @Override
    public Map<String, Boolean> checkToken(Map<String, String> request) throws Exception {
        String jwtToken = request.get("token");

        if (jwtToken == null || jwtToken.isEmpty()) {
            throw new Exception("Token cannot empty!");
        }

        String username = jwtUtil.extractUsername(jwtToken);

        if (username != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwtToken, userDetails)) {
                return Collections.singletonMap("isValid", true);
            }
        }

        throw new Exception("Token are invalid!");
    }

}
