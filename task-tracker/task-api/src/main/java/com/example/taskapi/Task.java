// Task.java
package com.example.taskapi;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
  public String title;
  public String status = "OPEN";
  public LocalDate dueDate;
  public Instant completedAt;
}

