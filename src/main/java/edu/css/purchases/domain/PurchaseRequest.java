package edu.css.purchases.domain;

public record PurchaseRequest(RequestId id, String businessKey, RequestStatus status, String title) {}
