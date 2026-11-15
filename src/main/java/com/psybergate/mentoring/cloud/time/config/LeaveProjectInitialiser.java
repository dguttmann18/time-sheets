package com.psybergate.mentoring.cloud.time.config;

import com.psybergate.mentoring.cloud.time.entity.Project;
import com.psybergate.mentoring.cloud.time.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class LeaveProjectInitialiser {

  public static final String LEAVE_PROJECT_NAME = "Leave";

  private final ProjectRepository projectRepository;

  @Bean
  ApplicationRunner initialiseLeaveProject() {
    return args -> {
      if (projectRepository.findByName(LEAVE_PROJECT_NAME).isEmpty()) {
        Project p = new Project();
        p.setName(LEAVE_PROJECT_NAME);
        p.setDescription("Auto-created for leave");
        p.setClient("Internal");
        projectRepository.save(p);
      }
    };
  }
}
