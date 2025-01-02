package org.u2soft.billtasticbackend.controller;

import org.u2soft.billtasticbackend.entity.User;
import org.u2soft.billtasticbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Kullanıcıları listeleme
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Kullanıcı oluşturma
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // Kullanıcıyı güncelleme
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    // Kullanıcıyı silme
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
