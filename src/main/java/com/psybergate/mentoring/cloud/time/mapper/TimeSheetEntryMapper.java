package com.psybergate.mentoring.cloud.time.mapper;

import com.psybergate.mentoring.cloud.time.dto.request.TimeSheetEntryRequest;
import com.psybergate.mentoring.cloud.time.dto.response.TimeSheetEntryResponse;
import com.psybergate.mentoring.cloud.time.entity.TimeSheetEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TimeSheetEntryMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "created", ignore = true)
  @Mapping(target = "updated", ignore = true)
  @Mapping(target = "active", ignore = true)
  TimeSheetEntry toEntity(TimeSheetEntryRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "created", ignore = true)
  @Mapping(target = "updated", ignore = true)
  @Mapping(target = "active", ignore = true)
  void updateEntityFromRequest(TimeSheetEntryRequest request, @MappingTarget TimeSheetEntry entity);

  TimeSheetEntryResponse toResponse(TimeSheetEntry entity);

  List<TimeSheetEntryResponse> toResponseList(List<TimeSheetEntry> entities);

}
