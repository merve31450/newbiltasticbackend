package org.u2soft.billtasticbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.u2soft.billtasticbackend.entity.Task;
import java.time.LocalDateTime;
import java.util.List;

@Repository



public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByRemindAtBeforeAndBadge(LocalDateTime time, Task.Badge badge);
}