package edu.css.purchases.handler;

import jakarta.validation.constraints.NotBlank;

public record PurchaseRequestRequest(
    @NotBlank String businessKey,
    @NotBlank String title) {}
