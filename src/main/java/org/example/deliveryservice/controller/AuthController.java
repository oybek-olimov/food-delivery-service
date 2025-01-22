package org.example.deliveryservice.controller;


import jakarta.validation.Valid;
import org.example.deliveryservice.configuration.JwtTokenUtil;
import org.example.deliveryservice.dto.authUserDto.AuthUserResponseDTO;
import org.example.deliveryservice.dto.authUserDto.CreateAuthUserDTO;
import org.example.deliveryservice.dto.authUserDto.UpdateAuthUserDTO;
import org.example.deliveryservice.dto.requestDto.LoginRequest;
import org.example.deliveryservice.dto.requestDto.OtpRequest;
import org.example.deliveryservice.service.impl.AuthUserServiceImpl;
import org.example.deliveryservice.service.impl.OtpServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthUserServiceImpl authUserServiceImpl;
    private final AuthenticationManager authenticationManager;
    private final OtpServiceImpl otpServiceImpl;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(AuthUserServiceImpl authUserServiceImpl, AuthenticationManager authenticationManager, OtpServiceImpl otpServiceImpl, JwtTokenUtil jwtTokenUtil) {
        this.authUserServiceImpl = authUserServiceImpl;
        this.authenticationManager = authenticationManager;
        this.otpServiceImpl = otpServiceImpl;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody CreateAuthUserDTO dto) {
        authUserServiceImpl.registerUser(dto);
        otpServiceImpl.sendOtp(dto.email());
        return ResponseEntity.status(201).build();

    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());
        authenticationManager.authenticate(authentication);
        otpServiceImpl.sendOtp(loginRequest.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpRequest otpRequest) {
        boolean isOtpValid = otpServiceImpl.validateOTP(otpRequest.getEmail(), otpRequest.getOtpCode());
        if (isOtpValid) {
            String token = jwtTokenUtil.generateToken(otpRequest.getEmail());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<AuthUserResponseDTO> createUser(@Valid @RequestBody CreateAuthUserDTO dto) {
        AuthUserResponseDTO user = authUserServiceImpl.registerUser(dto);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AuthUserResponseDTO>> getUsers() {
        List<AuthUserResponseDTO> allUsers = authUserServiceImpl.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthUserResponseDTO> getUserById(@PathVariable Long id) {
        AuthUserResponseDTO user = authUserServiceImpl.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update/{id}")
    public ResponseEntity<AuthUserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateAuthUserDTO updateAuthUserDTO) {
        AuthUserResponseDTO user = authUserServiceImpl.updateUser(id, updateAuthUserDTO);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        authUserServiceImpl.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }


}
