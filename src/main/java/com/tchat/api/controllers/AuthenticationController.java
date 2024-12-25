package com.tchat.api.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tchat.api.dto.requests.LoginRequestDto;
import com.tchat.api.dto.requests.RegisterRequestDto;
import com.tchat.api.services.AuthenticationService;
import com.tchat.api.utilities.BaseAppController;
import com.tchat.api.utilities.BaseResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController extends BaseAppController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponse> SignUp(@ModelAttribute RegisterRequestDto registerRequest) {
        try {
            return toResponse(authenticationService.registerUser(registerRequest), "Registration successfully!");
        } catch (Exception e) {
            return toResponse(e);
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<BaseResponse> signIn(@RequestBody LoginRequestDto request) {
        try {
            return toResponse(authenticationService.loginUser(request), "Login successfully!");
        } catch (Exception e) {
            return toResponse(e);
        }
    }

    @PostMapping("/check-token")
    public ResponseEntity<BaseResponse> checkToken(@RequestBody Map<String, String> request) {
        try {
            return toResponse(authenticationService.checkToken(request), "Token valid!");
        } catch (Exception e) {
            return toResponse(e);
        }
    }
}