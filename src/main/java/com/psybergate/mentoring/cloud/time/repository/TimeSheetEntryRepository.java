package com.psybergate.mentoring.cloud.time.repository;

import com.psybergate.mentoring.cloud.time.entity.Project;
import com.psybergate.mentoring.cloud.time.entity.TimeSheetEntry;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimeSheetEntryRepository extends JpaRepository<TimeSheetEntry, Long> {

  Optional<TimeSheetEntry> findByIdAndActiveTrue(Long id);

  List<TimeSheetEntry> findByActiveTrue(Sort sort);

  List<TimeSheetEntry> findByActiveTrueAndDateOfEntryBetween(LocalDate from, LocalDate to, Sort sort);

  List<TimeSheetEntry> findByActiveTrueAndProject(Project project, Sort sort);

  List<TimeSheetEntry> findByActiveTrueAndDateOfEntry(LocalDate date);

}
