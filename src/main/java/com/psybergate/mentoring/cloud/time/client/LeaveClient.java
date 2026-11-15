package com.psybergate.mentoring.cloud.time.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class LeaveClient {

  private final RestClient leaveRestClient;

  public void addLeaveHours(double hours) {
    leaveRestClient.patch()
            .uri(u -> u.path("/api/leave_due/add_hours").queryParam("hours", hours).build())
            .retrieve()
            .toBodilessEntity();
  }

}
