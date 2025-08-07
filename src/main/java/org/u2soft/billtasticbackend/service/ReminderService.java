package org.u2soft.billtasticbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.u2soft.billtasticbackend.entity.Task;
import org.u2soft.billtasticbackend.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderService {

    private final TaskRepository taskRepository;

    @Scheduled(fixedRate = 10000) // 🔥 10 saniyede bir kontrol (60000'di)
    public void checkDueReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<Task> tasks = taskRepository.findByRemindAtBeforeAndBadge(now, Task.Badge.PENDING);

        if (tasks.isEmpty()) {
            log.info("Hatırlatıcı zamanı: Görev bulunamadı");
            return;
        }

        tasks.forEach(t -> {
            log.info("Hatırlatma zamanı geldi: {}", t.getTask());
            t.setBadge(Task.Badge.ARCHIVED);
        });

        taskRepository.saveAll(tasks);
    }
}
