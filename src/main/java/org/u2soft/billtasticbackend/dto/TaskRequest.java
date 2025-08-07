package org.u2soft.billtasticbackend.dto;

import org.u2soft.billtasticbackend.entity.Task;

import java.time.LocalDateTime;

public record TaskRequest(
        String         task,
        String         collection,
        String         description,
        Task.Badge     badge,
        Long           categoryId,
        LocalDateTime  remindAt,
        boolean        completed
) {}
