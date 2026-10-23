package edu.css.purchases.persistence;

import edu.css.purchases.domain.RequestStatus;
import java.util.UUID;

public final class RequestMapper {

  private RequestMapper() {}

  public static RequestEntity toEntity(UUID id, String key, RequestStatus status, String title) {
    String column = switch (status) {
      case DRAFT -> "DRAFT";
      case APPROVED -> "APPROVED";
      case ORDERED -> "ORDERED";
    };
    return new RequestEntity(id, key, column, title);
  }

  public static RequestStatus toStatus(String column) {
    return switch (column) {
      case "DRAFT" -> RequestStatus.DRAFT;
      case "APPROVED" -> RequestStatus.APPROVED;
      case "ORDERED" -> RequestStatus.ORDERED;
      default -> throw new IllegalStateException("unknown status in database: " + column);
    };
  }
}