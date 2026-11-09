package com.psybergate.mentoring.cloud.time.repository;

import com.psybergate.mentoring.cloud.time.entity.TimeSheetEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimeSheetEntryRepository extends JpaRepository<TimeSheetEntry, Long> {

  Optional<TimeSheetEntry> findByIdAndActiveTrue(Long id);

  List<TimeSheetEntry> findByActiveTrue();

  List<TimeSheetEntry> findByActiveTrueAndDateOfEntryBetween(LocalDate from, LocalDate to);

  List<TimeSheetEntry> findByActiveTrueAndProjectId(Long projectId);

  List<TimeSheetEntry> findByActiveTrueAndDateOfEntry(LocalDate date);

}
