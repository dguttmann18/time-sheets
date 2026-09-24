package com.psybergate.mentoring.cloud.time.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@Data
@Entity
@Table(name = "projects")
public class Project {

  @Column(name = "name")
  private String name;

  private String description;

}
