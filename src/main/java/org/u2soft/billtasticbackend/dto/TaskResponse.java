package org.u2soft.billtasticbackend.dto;

import java.time.LocalDateTime;
import org.u2soft.billtasticbackend.entity.Task;

public record TaskResponse(
        Long                 id,
        String               task,
        String               collection,
        String               description,
        Task.Badge           badge,
        TaskCategoryResponse category,
        LocalDateTime        remindAt
) {
    public TaskResponse(Task t) {
        this(
                t.getId(), t.getTask(), t.getCollection(), t.getDescription(),
                t.getBadge(),
                t.getCategory() != null ? new TaskCategoryResponse(t.getCategory()) : null,
                t.getRemindAt()
        );
    }
}
