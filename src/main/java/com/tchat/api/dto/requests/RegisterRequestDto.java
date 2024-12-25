package com.tchat.api.dto.requests;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDto {
    private String fullName;
    private String email;
    private String username;
    private String password;
    private MultipartFile imageProfile;
}
