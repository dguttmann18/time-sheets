package com.psybergate.mentoring.cloud.time.service.impl;

import com.psybergate.mentoring.cloud.time.dto.TimeSheetEntryRequest;
import com.psybergate.mentoring.cloud.time.dto.TimeSheetEntryResponse;
import com.psybergate.mentoring.cloud.time.service.TimeSheetEntryService;

import java.time.LocalDate;
import java.util.List;

public class TimeSheetEntryServiceImpl implements TimeSheetEntryService {

  @Override
  public TimeSheetEntryResponse save(TimeSheetEntryRequest TimeSheetEntry) {
    return null;
  }

  @Override
  public TimeSheetEntryResponse update(TimeSheetEntryRequest TimeSheetEntry, Long id) {
    return null;
  }

  @Override
  public void delete(Long id) {

  }

  @Override
  public TimeSheetEntryResponse get(Long id) {
    return null;
  }

  @Override
  public List<TimeSheetEntryResponse> getAll() {
    return List.of();
  }

  @Override
  public List<TimeSheetEntryResponse> getAllContaining(String value) {
    return List.of();
  }

  @Override
  public List<TimeSheetEntryResponse> getAllOnDate(LocalDate date) {
    return List.of();
  }
}
