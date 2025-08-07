package org.u2soft.billtasticbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.u2soft.billtasticbackend.dto.RegisterRequest;
import org.u2soft.billtasticbackend.entity.Role;
import org.u2soft.billtasticbackend.entity.User;
import org.u2soft.billtasticbackend.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /* ---------- REGISTER ---------- */
    public User registerUser(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("Bu e-posta zaten kayıtlı.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole().toUpperCase())); // USER / ADMIN

        return userRepository.save(user);
    }

    /* ---------- DİĞER CRUD METOTLARI ---------- */

    public List<User> getAllUsers() { return userRepository.findAll(); }

    public User createUser(User user) { return userRepository.save(user); }

    public User updateUser(Long id, User user) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            user.setId(id);
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(Long id) { userRepository.deleteById(id); }
}
