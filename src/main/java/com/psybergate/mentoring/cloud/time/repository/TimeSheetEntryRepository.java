package com.psybergate.mentoring.cloud.time.repository;

import com.psybergate.mentoring.cloud.time.entity.TimeSheetEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSheetEntryRepository extends JpaRepository<TimeSheetEntry, Long> {

}
