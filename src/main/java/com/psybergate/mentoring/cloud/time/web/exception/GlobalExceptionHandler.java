package com.psybergate.mentoring.cloud.time.web.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ProblemDetail handleNotFound(EntityNotFoundException ex) {
    log.warn("Not found: {}", ex.getMessage());
    return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ProblemDetail handleBadRequest(IllegalArgumentException ex) {
    log.warn("Bad request: {}", ex.getMessage());
    return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(IllegalStateException.class)
  public ProblemDetail handleConflict(IllegalStateException ex) {
    log.warn("Conflict: {}", ex.getMessage());
    return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
  }

  @ExceptionHandler(RestClientResponseException.class)
  public ProblemDetail handleLeaveServiceError(RestClientResponseException ex) {
    log.warn("Remote call to leave service failed with {}: {}",
            ex.getStatusCode(), ex.getResponseBodyAsString());
    String detail = "Leave service returned an error";
    try {
      ProblemDetail downstream = ex.getResponseBodyAs(ProblemDetail.class);
      if (downstream != null && downstream.getDetail() != null) {
        detail = "Leave: " + downstream.getDetail();
      }
    } catch (Exception ignored) {}
    return ProblemDetail.forStatusAndDetail(ex.getStatusCode(), detail);
  }

  @ExceptionHandler(ResourceAccessException.class)
  public ProblemDetail handleLeaveServiceUnavailable(ResourceAccessException ex) {
    log.error("Leave service unreachable", ex);
    return ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE,
            "Leave service is unavailable. Please try again later.");
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleUnexpected(Exception ex) {
    log.error("Unexpected error", ex);
    return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
          Exception ex,
          Object body,
          HttpHeaders headers,
          HttpStatusCode statusCode,
          WebRequest request) {
    log.warn("{} {}: {}", statusCode.value(), ex.getClass().getSimpleName(), ex.getMessage());
    return super.handleExceptionInternal(ex, body, headers, statusCode, request);
  }
}
