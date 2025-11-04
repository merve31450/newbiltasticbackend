package org.u2soft.billtasticbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.u2soft.billtasticbackend.entity.User;
import org.u2soft.billtasticbackend.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /*  Admin paneli için: tüm kullanıcılar */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /*  Yeni kullanıcı oluştur */
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    /*  Var olan kullanıcıyı güncelle (admin için) */
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    /* Kullanıcı sil (admin için) */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    /*  Giriş yapan kullanıcı bilgilerini getir */
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getCurrentUser(authentication));
    }
    @PostMapping("/me/photo")
    public ResponseEntity<String> uploadProfilePhoto(
            Authentication authentication,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            userService.saveProfileImage(authentication, file);
            return ResponseEntity.ok("Fotoğraf başarıyla kaydedildi");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Fotoğraf yüklenemedi: " + e.getMessage());
        }
    }

    /*  Sadece şifreyi değiştir */
    @PutMapping("/me")
    public ResponseEntity<User> updatePassword(
            Authentication authentication,
            @RequestBody User updatedUser
    ) {
        return ResponseEntity.ok(userService.updatePassword(authentication, updatedUser.getPassword()));
    }
}
