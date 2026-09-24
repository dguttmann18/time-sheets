package com.psybergate.mentoring.cloud.time.mapper;

import com.psybergate.mentoring.cloud.time.dto.request.ProjectRequest;
import com.psybergate.mentoring.cloud.time.dto.response.ProjectResponse;
import com.psybergate.mentoring.cloud.time.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "created", ignore = true)
  @Mapping(target = "updated", ignore = true)
  @Mapping(target = "active", ignore = true)
  Project toEntity(ProjectRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "created", ignore = true)
  @Mapping(target = "updated", ignore = true)
  @Mapping(target = "active", ignore = true)
  void updateEntityFromRequest(ProjectRequest request, @MappingTarget Project entity);

  ProjectResponse toResponse(Project entity);

  List<ProjectResponse> toResponseList(List<Project> entities);
}
