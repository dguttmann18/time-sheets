package com.psybergate.mentoring.cloud.time.service.impl;

import com.psybergate.mentoring.cloud.time.client.LeaveClient;
import com.psybergate.mentoring.cloud.time.config.LeaveProjectInitialiser;
import com.psybergate.mentoring.cloud.time.dto.request.LeaveTimeSheetRequest;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
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
    if (LeaveProjectInitialiser.LEAVE_PROJECT_NAME.equals(project.getName()))
      return;
    if (hours == 0) return;
    double leaveHours = Math.round(hours * accrualRate * 100) / 100.0;
    leaveClient.addLeaveHours(leaveHours);
  }

  @Transactional
  @Override
  public TimeSheetEntryResponse save(TimeSheetEntryRequest request) {
    TimeSheetEntry entity = mapper.toEntity(request);
    Project project = projectRepository.findByIdAndActiveTrue(request.getProjectId())
            .orElseThrow(() -> new EntityNotFoundException("Project with id {" + request.getProjectId() + "} not found"));
    rejectLeaveProject(project);
    entity.setProject(project);
    TimeSheetEntry saved = repository.save(entity);
    accrueLeave(project, saved.getHours());
    return mapper.toResponse(saved);
  }

  @Transactional
  @Override
  public TimeSheetEntryResponse update(TimeSheetEntryRequest request, Long id) {
    TimeSheetEntry entity = repository.findByIdAndActiveTrue(id)
            .orElseThrow(() -> new EntityNotFoundException("Time sheet entry with id {" + id + "} not found"));
    if (entity.getLeaveApplicationId() != null)
      throw new IllegalStateException(
              "Leave entries must be changed through the leave application"
      );
    Project oldProject = entity.getProject();
    double oldHours = entity.getHours();
    Long projectId = request.getProjectId();
    Project newProject = projectRepository.findByIdAndActiveTrue(projectId)
            .orElseThrow(() -> new EntityNotFoundException("Project with id {" + projectId + "} not found"));
    rejectLeaveProject(newProject);
    mapper.updateEntityFromRequest(request, entity);
    entity.setProject(newProject);
    accrueLeave(oldProject, -oldHours);
    accrueLeave(newProject, entity.getHours());
    return mapper.toResponse(entity);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    TimeSheetEntry entity = repository.findByIdAndActiveTrue(id)
            .orElseThrow(() -> new EntityNotFoundException("Time sheet entry with id{" + id + "} not found"));
    if (entity.getLeaveApplicationId() != null)
      throw new IllegalStateException(
              "Leave entries must be changed through the leave application"
      );
    accrueLeave(entity.getProject(), entity.getHours() * -1);
    entity.setActive(false);
  }

  @Override
  @Transactional
  public void createLeaveEntries(LeaveTimeSheetRequest request) {
    validateLeaveRequest(request);
    Project leaveProject = projectRepository
            .findByName(LeaveProjectInitialiser.LEAVE_PROJECT_NAME)
            .orElseThrow(() -> new EntityNotFoundException(
                    "Project {" + LeaveProjectInitialiser.LEAVE_PROJECT_NAME
                            + "} not found"));
    deleteLeaveEntries(request.getLeaveApplicationId());
    List<LocalDate> workingDays = request.getFirstDay()
            .datesUntil(request.getLastDay().plusDays(1))
            .filter(this::isWorkingDay)
            .toList();
    if (workingDays.isEmpty())
      throw new IllegalArgumentException("Leave from "
              + request.getFirstDay() + " to " + request.getLastDay() + " contains no working days");
    double hoursPerDay = request.getHours() / workingDays.size();
    List<TimeSheetEntry> entries = workingDays.stream()
            .map(day -> toLeaveEntry(day, hoursPerDay, leaveProject, request))
            .toList();
    repository.saveAll(entries);
  }

  @Override
  @Transactional
  public void deleteLeaveEntries(Long leaveApplicationId) {
    repository.findByLeaveApplicationIdAndActiveTrue(leaveApplicationId)
            .forEach(entry -> entry.setActive(false));
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

  private void validateLeaveRequest(LeaveTimeSheetRequest request) {
    if (request.getLeaveApplicationId() == null) {
      throw new IllegalArgumentException("leaveApplicationId is required");
    }
    if (request.getFirstDay() == null || request.getLastDay() == null) {
      throw new IllegalArgumentException("firstDay and lastDay are required");
    }
    if (request.getLastDay().isBefore(request.getFirstDay())) {
      throw new IllegalArgumentException("lastDay cannot be before firstDay");
    }
    if (request.getHours() <= 0) {
      throw new IllegalArgumentException("hours must be greater than 0");
    }
  }

  private boolean isWorkingDay(LocalDate date) {
    DayOfWeek day = date.getDayOfWeek();
    return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
  }

  private void rejectLeaveProject(Project project) {
    if (LeaveProjectInitialiser.LEAVE_PROJECT_NAME.equals(project.getName())) {
      throw new IllegalArgumentException(
              "Time cannot be logged against the " + LeaveProjectInitialiser.LEAVE_PROJECT_NAME
                      + " project directly. Apply for leave through the leave application instead"
      );
    }
  }

  private TimeSheetEntry toLeaveEntry(
          LocalDate day,
          double hours,
          Project leaveProject,
          LeaveTimeSheetRequest request
  ) {
    TimeSheetEntry entry = new TimeSheetEntry();
    entry.setDateOfEntry(day);
    entry.setHours(hours);
    entry.setProject(leaveProject);
    entry.setBillable(false);
    entry.setDescription(request.getDescription());
    entry.setLeaveApplicationId(request.getLeaveApplicationId());
    return entry;
  }
}
