// src/main/java/org/u2soft/billtasticbackend/controller/TaskController.java
package org.u2soft.billtasticbackend.controller;
import org.u2soft.billtasticbackend.dto.TaskRequest;
import org.u2soft.billtasticbackend.dto.TaskResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.u2soft.billtasticbackend.dto.TaskRequest;
import org.u2soft.billtasticbackend.dto.TaskResponse;
import org.u2soft.billtasticbackend.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:3000")     // portunuza göre ayarlayın
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @GetMapping
    public List<TaskResponse> all() {
        return service.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(@RequestBody @Valid TaskRequest req) {
        return service.create(req);
    }
}
