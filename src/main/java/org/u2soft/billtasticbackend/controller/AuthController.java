package org.u2soft.billtasticbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.u2soft.billtasticbackend.dto.AuthResponse;
import org.u2soft.billtasticbackend.dto.LoginRequest;
import org.u2soft.billtasticbackend.dto.RegisterRequest;
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


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        System.out.println(">>> LOGIN endpoint tetiklendi");

        // 1. Veritabanında kullanıcıyı e-posta ile bul
        org.u2soft.billtasticbackend.entity.User userEntity = userRepository.findByEmail(request.getEmail());
        if (userEntity == null) {
            System.out.println(request.getEmail());
            System.out.println(">>> Kullanıcı bulunamadı: " + request.getEmail());
            return ResponseEntity.status(401).body("Kullanıcı bulunamadı.");
        }

        // 2. DEBUG: Password encoder sınıfı
        System.out.println(">>> Kullanılan PasswordEncoder: " + passwordEncoder.getClass().getName());

        // 3. DEBUG: Veritabanındaki hash’lenmiş şifre bilgisi
        String dbHash = userEntity.getPassword();
        System.out.println(">>> DB hash: [" + dbHash + "]");
        System.out.println(">>> DB hash length: " + (dbHash == null ? "null" : dbHash.length()));
        System.out.println(">>> DB hash has trailing space? " + (dbHash != null && !dbHash.equals(dbHash.trim())));

        // 4. DEBUG: Kullanıcının girdiği şifre
        String rawPw = request.getPassword();
        System.out.println(">>> RAW şifre: [" + rawPw + "]");
        System.out.println(">>> RAW şifre uzunluğu: " + (rawPw == null ? "null" : rawPw.length()));

        // 5. DEBUG: Bilinen bir geçerli hash ile test
        String exampleHash = "$2a$10$IXqJf7X1Y3VOg/IBxl/AkOuIILWTk6rEtu3GBrT9wzkM/ssS93kKS"; // örnek
        System.out.println(">>> '1234' matches exampleHash? " + passwordEncoder.matches("1234", exampleHash));

        // 6. Şifre eşleşiyor mu?
        boolean passwordMatch = passwordEncoder.matches(rawPw, dbHash);
        System.out.println(">>> Eşleşme (orijinal hash): " + passwordMatch);


        if (!passwordMatch) {
            // trim'li test yapalım
            boolean trimmedMatch = passwordEncoder.matches(rawPw, dbHash.trim());
            System.out.println(">>> Eşleşme (trim uygulanmış hash): " + trimmedMatch);

            return ResponseEntity.status(401).body("Şifre yanlış.");
        }

        // 7. OTOMATİK Spring Security doğrulaması
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            rawPw
                    )
            );

            // 8. Doğrulama başarılı → kullanıcıyı al
            User user = (User) authentication.getPrincipal();

            // 9. JWT token üret
            String token = jwtUtil.generateToken(user);
            System.out.println(">>> JWT Token başarıyla üretildi.");

            // 10. Token'ı geri döndür
            return ResponseEntity.ok(new AuthResponse(token));

        } catch (BadCredentialsException e) {
            System.out.println(">>> AuthenticationManager doğrulama başarısız oldu.");
            return ResponseEntity.status(401).body("Giriş başarısız.");
        }
    }
}
