package com.psybergate.mentoring.cloud.time.repository;

import com.psybergate.mentoring.cloud.time.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
