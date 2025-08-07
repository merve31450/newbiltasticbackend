package org.u2soft.billtasticbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.u2soft.billtasticbackend.entity.TaskCategory;
import java.util.List;

public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Long> {
    List<TaskCategory> findByType(TaskCategory.Type type);
}
