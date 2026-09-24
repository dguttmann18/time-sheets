package com.psybergate.mentoring.cloud.time.web.controller;

import com.psybergate.mentoring.cloud.time.dto.request.TimeSheetEntryRequest;
import com.psybergate.mentoring.cloud.time.dto.response.TimeSheetEntryResponse;
import com.psybergate.mentoring.cloud.time.service.TimeSheetEntryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/time_sheet_entry")
public class TimeSheetEntryController {
  
  private final TimeSheetEntryService service;

  @PostMapping("/new")
  public void addNewTimeSheetEntry(@RequestBody TimeSheetEntryRequest request) {
    service.save(request);
  }

  @PatchMapping("/update/{id}")
  public void updateTimeSheetEntry(@RequestBody TimeSheetEntryRequest request, @PathVariable Long id) {
    service.update(request, id);
  }

  @GetMapping("/{id}")
  public TimeSheetEntryResponse getTimeSheetEntry(@PathVariable Long id) {
    return service.get(id);
  }
}
