package edu.css.purchases.handler;

import edu.css.purchases.domain.RequestStatus;

public record PurchaseRequestResponse(
    String id,
    String businessKey,
    RequestStatus status,
    String title) {}
