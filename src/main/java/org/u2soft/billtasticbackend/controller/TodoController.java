package org.u2soft.billtasticbackend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.u2soft.billtasticbackend.entity.Todo;
import org.u2soft.billtasticbackend.service.TodoService;

import java.util.List;

public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // Tüm Todo'ları getiren metod
    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    // Yeni Todo ekleme metodu
    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        Todo createdTodo = todoService.createTodo(todo);
        return new ResponseEntity<>(createdTodo, HttpStatus.CREATED); // Başarılı olursa 201 döner
    }

    // Todo güncelleme metodu
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        Todo updatedTodo = todoService.updateTodo(id, todo);
        if (updatedTodo != null) {
            return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Eğer Todo bulunamazsa 404 döner
    }

    // Todo silme metodu
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        boolean isDeleted = todoService.deleteTodo(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Başarılı olursa 204 döner
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Eğer Todo bulunamazsa 404 döner
    }
}
