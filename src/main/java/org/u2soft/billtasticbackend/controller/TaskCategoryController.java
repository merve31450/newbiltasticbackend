// src/main/java/org/u2soft/billtasticbackend/controller/TaskCategoryController.java
package org.u2soft.billtasticbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.u2soft.billtasticbackend.dto.TaskCategoryResponse;
import org.u2soft.billtasticbackend.entity.TaskCategory;        // ← doğru import
import org.u2soft.billtasticbackend.service.TaskCategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/task-categories")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class TaskCategoryController {

    private final TaskCategoryService service;


    @GetMapping
    public List<TaskCategoryResponse> all() {
        return service.findAll();              // eğer böyle bir metot yoksa ihtiyacına göre değiştir
    }


    @GetMapping("/work")
    public List<TaskCategoryResponse> work() {
        return service.findAllByType(TaskCategory.Type.WORK);   // enum sabiti = WORK
    }


    @GetMapping("/personal")
    public List<TaskCategoryResponse> personal() {
        return service.findAllByType(TaskCategory.Type.PERSONAL);
    }


    @GetMapping("/study")
    public List<TaskCategoryResponse> study() {
        return service.findAllByType(TaskCategory.Type.STUDY);
    }
}
