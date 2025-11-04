package org.u2soft.billtasticbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.u2soft.billtasticbackend.dto.RegisterRequest;
import org.u2soft.billtasticbackend.entity.Role;
import org.u2soft.billtasticbackend.entity.User;
import org.u2soft.billtasticbackend.repository.UserRepository;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /* 🔹 Kullanıcı kaydı (register) */
    public User registerUser(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("Bu e-posta zaten kayıtlı.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));

        return userRepository.save(user);
    }

    /* 🔹 Admin işlemleri */
    public List<User> getAllUsers() { return userRepository.findAll(); }

    public User createUser(User user) { return userRepository.save(user); }

    public User updateUser(Long id, User user) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            user.setId(id);
            return userRepository.save(user);
        }
        throw new RuntimeException("Kullanıcı bulunamadı");
    }

    public void deleteUser(Long id) { userRepository.deleteById(id); }

    /* ✅ Oturum açmış kullanıcı bilgisi */
    public User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) throw new RuntimeException("Kullanıcı bulunamadı: " + email);
        return user;
    }

    /* ✅ Şifre güncelle */
    public User updatePassword(Authentication authentication, String newPassword) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("Kullanıcı bulunamadı: " + email);
        }

        if (newPassword != null && !newPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        return userRepository.save(user);
    }


    public String saveProfileImage(Authentication authentication, MultipartFile file) throws IOException {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) throw new RuntimeException("Kullanıcı bulunamadı");

        String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
        user.setProfileImage(base64Image);
        userRepository.save(user);

        return "Profil fotoğrafı başarıyla güncellendi.";
    }

}
