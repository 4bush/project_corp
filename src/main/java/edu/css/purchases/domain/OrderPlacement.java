package edu.css.purchases.domain;

/** Moves a request to ORDERED and only then tells the supplier. */
public class OrderPlacement {

  private final RequestPolicy policy;
  private final SupplierOrderPort supplier;

  public OrderPlacement(RequestPolicy policy, SupplierOrderPort supplier) {
    this.policy = policy;
    this.supplier = supplier;
  }

  public RequestStatus order(RequestId id, RequestStatus current) {
    RequestStatus next = policy.move(current, RequestStatus.ORDERED);
    supplier.place(id);
    return next;
  }
}
