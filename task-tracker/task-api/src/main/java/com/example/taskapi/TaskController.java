// TaskController.java
package com.example.taskapi;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
  public record CreateTaskRequest(@NotBlank @Size(max = 200) String title, LocalDate dueDate) {}

  private final TaskService service;

  public TaskController(TaskService service) { this.service = service; }

  @GetMapping public List<Task> list() { return service.findAll(); }
  @GetMapping("/{id}") public Task get(@PathVariable long id) { return service.get(id); }

  @PostMapping @ResponseStatus(HttpStatus.CREATED)
  public Task create(@Valid @RequestBody CreateTaskRequest request) {
    return service.create(request.title(), request.dueDate());
  }

  @PostMapping("/{id}/complete") public Task complete(@PathVariable long id) { return service.complete(id); }

  @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable long id) { service.delete(id); }

  @Configuration
  static class ClockConfig {
    @Bean Clock clock() { return Clock.systemUTC(); }
  }
}