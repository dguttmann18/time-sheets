package com.psybergate.mentoring.cloud.time.service.impl;

import com.psybergate.mentoring.cloud.time.dto.ProjectRequest;
import com.psybergate.mentoring.cloud.time.dto.ProjectResponse;
import com.psybergate.mentoring.cloud.time.entity.Project;
import com.psybergate.mentoring.cloud.time.mapper.ProjectMapper;
import com.psybergate.mentoring.cloud.time.repository.ProjectRepository;
import com.psybergate.mentoring.cloud.time.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Data
@Service
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

  private final ProjectRepository repository;

  private final ProjectMapper mapper;

  @Override
  public void save(ProjectRequest project) {
    Project entity = mapper.toEntity(project);
    repository.save(entity);
  }

  @Override
  public void update(ProjectRequest project, Long id) {
    Project entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Project with id {" + id + "} not found"));

    mapper.updateEntityFromRequest(project, entity);
    repository.save(entity);
  }

  @Override
  public void delete(Long id) {

  }

  @Override
  public ProjectResponse get(Long id) {
    Project entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Project with id {" + id + "} not found"));

    return mapper.toResponse(entity);
  }

  @Override
  public List<ProjectResponse> getAll() {
    return mapper.toResponseList(repository.findAll());
  }

  @Override
  public List<ProjectResponse> getAllContaining(String value) {
    return List.of();
  }
}
