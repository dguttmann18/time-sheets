package com.psybergate.mentoring.cloud.time.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveTimeSheetRequest {

  private Long leaveApplicationId;

  private LocalDate firstDay;

  private LocalDate lastDay;

  private double hours;

  private String description;

}
