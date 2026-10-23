package edu.css.purchases.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "purchase_request")
public class RequestEntity {

  @Id
  private UUID id;

  @Column(name = "business_key", nullable = false, unique = true)
  private String businessKey;

  @Column(nullable = false)
  private String status;

  @Column(nullable = false)
  private String title;

  protected RequestEntity() {}

  public RequestEntity(UUID id, String businessKey, String status, String title) {
    this.id = id;
    this.businessKey = businessKey;
    this.status = status;
    this.title = title;
  }

  public UUID getId() {
    return id;
  }

  public String getBusinessKey() {
    return businessKey;
  }

  public String getStatus() {
    return status;
  }

  public String getTitle() {
    return title;
  }
}