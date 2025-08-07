package org.u2soft.billtasticbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.u2soft.billtasticbackend.dto.*;
import org.u2soft.billtasticbackend.entity.*;
import org.u2soft.billtasticbackend.repository.UserRepository;
import org.u2soft.billtasticbackend.security.JwtUtil;
import org.u2soft.billtasticbackend.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    /* ---------- LOGIN ---------- */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {

        org.u2soft.billtasticbackend.entity.User userEntity = userRepository.findByEmail(request.getEmail());
        if (userEntity == null) {
            return ResponseEntity.status(401).body("Kullanıcı bulunamadı.");
        }

        if (!passwordEncoder.matches(request.getPassword(), userEntity.getPassword())) {
            return ResponseEntity.status(401).body("Şifre yanlış.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = jwtUtil.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(userEntity.getEmail())
                        .password(userEntity.getPassword())
                        .authorities("ROLE_" + userEntity.getRole().name())
                        .build()
        );

        return ResponseEntity.ok(new AuthResponse(token));
    }

    /* ---------- REGISTER ---------- */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        try {
            org.u2soft.billtasticbackend.entity.User saved = userService.registerUser(request);
            return ResponseEntity.ok("Kayıt başarılı: " + saved.getEmail());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Kayıt başarısız: " + ex.getMessage());
        }
    }
}
