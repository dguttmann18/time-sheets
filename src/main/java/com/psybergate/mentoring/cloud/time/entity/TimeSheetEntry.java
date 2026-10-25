package com.psybergate.mentoring.cloud.time.entity;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class TimeSheetEntry extends BaseEntity {

  @Column(name = "date_of_entry")
  private LocalDate dateOfEntry;

  @ManyToOne
  @JoinColumn(name = "project_id")
  private Project project;

  private boolean billable;

  private double hours;

  private String description;
}
