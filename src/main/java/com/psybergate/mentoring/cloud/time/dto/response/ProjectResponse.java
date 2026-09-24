package com.psybergate.mentoring.cloud.time.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ProjectResponse {

  private Long id;

  private LocalDateTime created;

  private LocalDateTime updated;

  private boolean active;

  private String name;

  private String description;

}
