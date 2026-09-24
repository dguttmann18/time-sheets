package com.psybergate.mentoring.cloud.time.web.controller;

import com.psybergate.mentoring.cloud.time.dto.request.ProjectRequest;
import com.psybergate.mentoring.cloud.time.dto.response.ProjectResponse;
import com.psybergate.mentoring.cloud.time.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/project")
public class ProjectController {

  private final ProjectService service;

  @PostMapping("/new")
  public void addNewProject(@RequestBody ProjectRequest request) {
    service.save(request);
  }

  @PatchMapping("/update/{id}")
  public void updateProject(@RequestBody ProjectRequest request, @PathVariable Long id) {
    service.update(request, id);
  }

  @GetMapping("/{id}")
  public ProjectResponse getProject(@PathVariable Long id) {
    return service.get(id);
  }


}
