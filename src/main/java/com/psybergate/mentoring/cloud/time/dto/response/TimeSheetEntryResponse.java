package com.psybergate.mentoring.cloud.time.dto.response;

import com.psybergate.mentoring.cloud.time.entity.Project;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class TimeSheetEntryResponse {

  private Long id;

  private LocalDateTime created;

  private LocalDateTime updated;

  private boolean active;

  private LocalDate dateOfEntry;

  private Project project;

  private double hours;

  private boolean billable;

}
