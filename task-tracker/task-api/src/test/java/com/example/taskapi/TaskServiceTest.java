// task-api/src/test/java/com/example/taskapi/TaskServiceTest.java
package com.example.taskapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class TaskServiceTest {
  TaskRepository repository = mock(TaskRepository.class);
  Clock clock = Clock.fixed(Instant.parse("2026-03-02T10:00:00Z"), ZoneOffset.UTC);
  TaskService service = new TaskService(repository, clock);

  @Test
  void completingATaskMarksItDoneAtTheCurrentTime() {
    Task task = new Task();
    task.id = 1L;
    when(repository.findById(1L)).thenReturn(Optional.of(task));

    Task done = service.complete(1L);

    assertThat(done.status).isEqualTo("DONE");
    assertThat(done.completedAt).isEqualTo(clock.instant());
  }
}