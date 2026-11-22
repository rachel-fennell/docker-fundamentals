// TaskService.java
package com.example.taskapi;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TaskService {
  private final TaskRepository tasks;
  private final Clock clock;

  public TaskService(TaskRepository tasks, Clock clock) { this.tasks = tasks; this.clock = clock; }

  public List<Task> findAll() { return tasks.findAll(Sort.by("id")); }

  public Task get(long id) {
    return tasks.findById(id).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND, "Task " + id + " not found"));
  }

  @Transactional
  public Task create(String title, LocalDate dueDate) {
    Task task = new Task();
    task.title = title;
    task.dueDate = dueDate;
    return tasks.save(task);
  }

  @Transactional
  public Task complete(long id) {
    Task task = get(id);
    task.status = "DONE";
    task.completedAt = clock.instant();
    return task;
  }

  @Transactional
  public void delete(long id) { tasks.delete(get(id)); }
}