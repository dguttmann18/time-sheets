package com.psybergate.mentoring.cloud.time.service.impl;

import com.psybergate.mentoring.cloud.time.dto.request.ProjectRequest;
import com.psybergate.mentoring.cloud.time.dto.response.ProjectResponse;
import com.psybergate.mentoring.cloud.time.entity.Project;
import com.psybergate.mentoring.cloud.time.entity.TimeSheetEntry;
import com.psybergate.mentoring.cloud.time.mapper.ProjectMapper;
import com.psybergate.mentoring.cloud.time.repository.ProjectRepository;
import com.psybergate.mentoring.cloud.time.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

  private final ProjectRepository repository;

  private final ProjectMapper mapper;

  @Override
  @Transactional
  public void save(ProjectRequest project) {
    Project entity = mapper.toEntity(project);
    repository.save(entity);
  }

  @Override
  @Transactional
  public void update(ProjectRequest request, Long id) {
    Project entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Project with id {" + id + "} not found"));

    mapper.updateEntityFromRequest(request, entity);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    Project entity = repository.findByIdAndActiveTrue(id)
            .orElseThrow(() -> new EntityNotFoundException("Project with id{" + id + "} not found"));
    entity.setActive(false);
  }

  @Override
  public ProjectResponse get(Long id) {
    Project entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Project with id {" + id + "} not found"));
    return mapper.toResponse(entity);
  }

  @Override
  public List<ProjectResponse> getAll() {
    return mapper.toResponseList(repository.findByActiveTrue());
  }

  @Override
  public List<ProjectResponse> getAllContaining(String value) {
    return List.of();
  }
}
