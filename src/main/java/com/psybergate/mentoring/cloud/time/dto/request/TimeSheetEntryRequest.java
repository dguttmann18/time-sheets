package com.psybergate.mentoring.cloud.time.dto.request;

import com.psybergate.mentoring.cloud.time.entity.Project;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TimeSheetEntryRequest {

  private LocalDate dateOfEntry;

  private Project project;

  private double hours;

  private boolean billable;

}
