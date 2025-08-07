package org.u2soft.billtasticbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.u2soft.billtasticbackend.dto.TaskCategoryResponse;
import org.u2soft.billtasticbackend.entity.TaskCategory;
import org.u2soft.billtasticbackend.repository.TaskCategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskCategoryService {

    private final TaskCategoryRepository repo;


    public List<TaskCategoryResponse> findAllByType(TaskCategory.Type type) {
        return repo.findByType(type)
                .stream()
                .map(this::toDto)
                .toList();
    }


    public List<TaskCategoryResponse> findAll() {
        return repo.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }


    private TaskCategoryResponse toDto(TaskCategory c) {
        return new TaskCategoryResponse(
                c.getId(),
                c.getName(),
                c.getType(),
                c.getColor()
        );
    }
}
