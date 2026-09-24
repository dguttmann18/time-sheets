package com.psybergate.mentoring.cloud.time.service;

import com.psybergate.mentoring.cloud.time.dto.request.ProjectRequest;
import com.psybergate.mentoring.cloud.time.dto.response.ProjectResponse;

import java.util.List;

public interface ProjectService {

  void save(ProjectRequest project);

  void update(ProjectRequest project, Long id);

  void delete(Long id);

  ProjectResponse get(Long id);

  List<ProjectResponse> getAll();

  List<ProjectResponse> getAllContaining(String value);
}
