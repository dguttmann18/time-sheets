package com.psybergate.mentoring.cloud.time.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode
@NoArgsConstructor
@Data
@Entity
@Table(name = "time_sheet_entries")
public class TimeSheetEntry {

  @Column(name = "date_of_entry")
  private LocalDate dateOfEntry;

  private Project project;

  private double hours;

  private boolean billable;

}
