package com.psybergate.mentoring.cloud.time.repository;

import com.psybergate.mentoring.cloud.time.entity.Project;
import com.psybergate.mentoring.cloud.time.entity.TimeSheetEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

  Optional<Project> findByIdAndActiveTrue(Long id);

  List<Project> findByActiveTrue();

  List<Project> findByActiveFalse();
}
