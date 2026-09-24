package com.psybergate.mentoring.cloud.time.service;

import com.psybergate.mentoring.cloud.time.dto.request.TimeSheetEntryRequest;
import com.psybergate.mentoring.cloud.time.dto.response.TimeSheetEntryResponse;

import java.time.LocalDate;
import java.util.List;

public interface TimeSheetEntryService {

  TimeSheetEntryResponse save(TimeSheetEntryRequest TimeSheetEntry);

  TimeSheetEntryResponse update(TimeSheetEntryRequest TimeSheetEntry, Long id);

  void delete(Long id);

  TimeSheetEntryResponse get(Long id);

  List<TimeSheetEntryResponse> getAll();

  List<TimeSheetEntryResponse> getAllContaining(String value);

  List<TimeSheetEntryResponse> getAllOnDate(LocalDate date);
}
