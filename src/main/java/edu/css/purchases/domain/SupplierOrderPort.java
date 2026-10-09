package edu.css.purchases.domain;

/** Outbound port: the real adapter (HTTP to the supplier / ERP) comes in a later lab. */
public interface SupplierOrderPort {
  void place(RequestId id);
}
