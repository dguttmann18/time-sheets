package com.psybergate.mentoring.cloud.time.service.impl;

import com.psybergate.mentoring.cloud.time.client.LeaveClient;
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
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
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

  private final LeaveClient leaveClient;

  @Value("${leave.accrual-rate}")
  private double accrualRate;

  private void accrueLeave(Project project, double hours) {
    if ("Leave".equals(project.getName())) return;
    if (hours == 0) return;
    leaveClient.addLeaveHours(hours * accrualRate);
  }

  @Transactional
  @Override
  public TimeSheetEntryResponse save(TimeSheetEntryRequest request) {
    TimeSheetEntry entity = mapper.toEntity(request);
    Project project = projectRepository.findById(request.getProjectId())
            .orElseThrow(() -> new EntityNotFoundException("Project with id {" + request.getProjectId() + "} not found"));
    entity.setProject(project);
    accrueLeave(project, entity.getHours());
    return mapper.toResponse(repository.save(entity));
  }

  @Transactional
  @Override
  public TimeSheetEntryResponse update(TimeSheetEntryRequest request, Long id) {
    TimeSheetEntry entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Time sheet entry with id {" + id + "} not found"));
    double oldHours = entity.getHours();
    mapper.updateEntityFromRequest(request, entity);
    double newHours = entity.getHours();
    accrueLeave(entity.getProject(), newHours -oldHours);
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
    accrueLeave(entity.getProject(), entity.getHours() * -1);
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
  public List<TimeSheetEntryResponse> getAllActive() {
    return null;
  }

  @Override
  public List<TimeSheetEntryResponse> getAllContaining(String value) {
    return List.of();
  }

  @Override
  public List<TimeSheetEntryResponse> getAllOnDate(LocalDate date) {
    return null;
  }

  @Override
  public List<TimeSheetEntryResponse> getAllInRange(LocalDate firstDay, LocalDate lastDay, String orderBy, boolean isAscending) {
    List<TimeSheetEntry> list = isAscending
            ? repository.findByActiveTrueAndDateOfEntryBetween(firstDay, lastDay, Sort.by(Sort.Direction.ASC, orderBy))
            : repository.findByActiveTrueAndDateOfEntryBetween(firstDay, lastDay, Sort.by(Sort.Direction.DESC, orderBy));

    return mapper.toResponseList(list);
  }
}
