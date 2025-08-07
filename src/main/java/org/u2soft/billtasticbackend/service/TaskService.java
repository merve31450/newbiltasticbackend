package org.u2soft.billtasticbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.u2soft.billtasticbackend.dto.TaskRequest;
import org.u2soft.billtasticbackend.dto.TaskResponse;
import org.u2soft.billtasticbackend.entity.Task;
import org.u2soft.billtasticbackend.entity.TaskCategory;
import org.u2soft.billtasticbackend.repository.TaskCategoryRepository;
import org.u2soft.billtasticbackend.repository.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskCategoryRepository categoryRepository;


    public TaskResponse create(TaskRequest req) {
        Task task = Task.builder()
                .task(req.task())
                .collection(req.collection())
                .description(req.description())
                .badge(req.badge())
                .remindAt(req.remindAt())
                .build();


        if (req.categoryId() != null) {
            TaskCategory category = categoryRepository.findById(req.categoryId())
                    .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));
            task.setCategory(category);
        }

        return new TaskResponse(taskRepository.save(task));
    }


    public TaskResponse update(Long id, TaskRequest req) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Görev bulunamadı: " + id));

        task.setTask(req.task());
        task.setCollection(req.collection());
        task.setDescription(req.description());
        task.setBadge(req.badge());
        task.setRemindAt(req.remindAt());
        task.setCompleted(req.completed());

        return new TaskResponse(taskRepository.save(task));
    }



    public void delete(Long id) {
        taskRepository.deleteById(id);
    }


    public List<TaskResponse> getAll() {
        return taskRepository.findAll()
                .stream()
                .map(TaskResponse::new)
                .toList();
    }
}
