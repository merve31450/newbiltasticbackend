package org.u2soft.billtasticbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.u2soft.billtasticbackend.dto.TaskRequest;
import org.u2soft.billtasticbackend.dto.TaskResponse;
import org.u2soft.billtasticbackend.service.TaskService;
import org.u2soft.billtasticbackend.repository.TaskRepository;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskRepository taskRepository;

    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody TaskRequest req) {
        return ResponseEntity.ok(taskService.create(req));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAll() {
        return ResponseEntity.ok(taskService.getAll());
    }
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@PathVariable Long id, @RequestBody TaskRequest req) {
        return ResponseEntity.ok(taskService.update(id, req));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
