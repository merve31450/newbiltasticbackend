package org.u2soft.billtasticbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.u2soft.billtasticbackend.entity.TaskCategory;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCategoryResponse {

    private Long              id;
    private String            name;
    private TaskCategory.Type type;
    private String            color;

    public TaskCategoryResponse(TaskCategory c) {
        this(c.getId(), c.getName(), c.getType(), c.getColor());
    }
}
