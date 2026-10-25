package com.psybergate.mentoring.cloud.time.service.impl;

import com.psybergate.mentoring.cloud.time.dto.request.TimeSheetEntryRequest;
import com.psybergate.mentoring.cloud.time.dto.response.TimeSheetEntryResponse;
import com.psybergate.mentoring.cloud.time.entity.Project;
import com.psybergate.mentoring.cloud.time.entity.TimeSheetEntry;
import com.psybergate.mentoring.cloud.time.mapper.TimeSheetEntryMapper;
import com.psybergate.mentoring.cloud.time.repository.ProjectRepository;
import com.psybergate.mentoring.cloud.time.repository.TimeSheetEntryRepository;
import com.psybergate.mentoring.cloud.time.service.TimeSheetEntryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TimeSheetEntryServiceImpl implements TimeSheetEntryService {

  private final TimeSheetEntryRepository repository;

  private final TimeSheetEntryMapper mapper;

  private final ProjectRepository projectRepository;

  @Transactional
  @Override
  public TimeSheetEntryResponse save(TimeSheetEntryRequest request) {
    TimeSheetEntry entity = mapper.toEntity(request);
    Project project = projectRepository.findById(request.getProjectId())
            .orElseThrow(() -> new EntityNotFoundException("Project with id {" + request.getProjectId() + "} not found"));
    return mapper.toResponse(repository.save(entity));
  }

  @Transactional
  @Override
  public TimeSheetEntryResponse update(TimeSheetEntryRequest request, Long id) {
    TimeSheetEntry entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Time sheet entry with id {" + id + "} not found"));

    mapper.updateEntityFromRequest(request, entity);
    Long projectId = request.getProjectId();
    Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EntityNotFoundException("Project with id {" + projectId + "} not found"));
    entity.setProject(project);

    return mapper.toResponse(entity);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    TimeSheetEntry entity = repository.findByIdAndActiveTrue(id)
            .orElseThrow(() -> new EntityNotFoundException("Time sheet entry with id{" + id + "} not found"));
    entity.setActive(false);
  }

  @Override
  public TimeSheetEntryResponse get(Long id) {
    return null;
  }

  @Override
  public List<TimeSheetEntryResponse> getAll() {
    return mapper.toResponseList(repository.findAll());
  }

  @Override
  public List<TimeSheetEntryResponse> getAllContaining(String value) {
    return List.of();
  }

  @Override
  public List<TimeSheetEntryResponse> getAllOnDate(LocalDate date) {
    return mapper.toResponseList(repository.findByActiveTrueAndDateOfEntry(date));
  }
}
