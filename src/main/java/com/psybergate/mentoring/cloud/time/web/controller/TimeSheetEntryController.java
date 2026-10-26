package com.psybergate.mentoring.cloud.time.web.controller;

import com.psybergate.mentoring.cloud.time.dto.request.TimeSheetEntryRequest;
import com.psybergate.mentoring.cloud.time.dto.response.TimeSheetEntryResponse;
import com.psybergate.mentoring.cloud.time.service.TimeSheetEntryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/time_sheet_entry")
public class TimeSheetEntryController {
  
  private final TimeSheetEntryService service;

  @GetMapping("/all")
  public List<TimeSheetEntryResponse> getAllTimesheets() {
    return service.getAll();
  }

  @PostMapping("/new")
  public void addNewTimeSheetEntry(@RequestBody TimeSheetEntryRequest request) {
    service.save(request);
  }

  @PatchMapping("/update/{id}")
  public TimeSheetEntryResponse updateTimeSheetEntry(@RequestBody TimeSheetEntryRequest request, @PathVariable Long id) {
    return service.update(request, id);
  }

  @GetMapping("/{id}")
  public TimeSheetEntryResponse getTimeSheetEntry(@PathVariable Long id) {
    return service.get(id);
  }

  @DeleteMapping("/{id}")
  public void deleteTimeSheetEntry(@PathVariable Long id) {
    service.delete(id);
  }
}
