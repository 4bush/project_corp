package edu.css.purchases.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class RequestPolicyTest {

  private final RequestPolicy policy = new RequestPolicy(
      new ChainRule(List.of(new UnapprovedCannotOrder(), new TransitionRule())));

  // The four rows of the README status table
  @ParameterizedTest(name = "{0} -> {1}, allowed={2}")
  @CsvSource({
      "DRAFT,APPROVED,true",
      "APPROVED,ORDERED,true",
      "DRAFT,ORDERED,false",
      "ORDERED,APPROVED,false"
  })
  void statusTable(RequestStatus from, RequestStatus to, boolean allowed) {
    if (allowed) {
      assertEquals(to, policy.move(from, to));
    } else {
      assertThrows(IllegalStateException.class, () -> policy.move(from, to));
    }
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "   "})
  void blankIdThrows(String value) {
    assertThrows(IllegalArgumentException.class, () -> new RequestId(value));
  }

  @Test
  void orderingAnApprovedRequestCallsTheSupplier() {
    SupplierOrderPort supplier = mock(SupplierOrderPort.class);
    OrderPlacement placement = new OrderPlacement(policy, supplier);
    RequestId id = RequestId.newId();

    assertEquals(RequestStatus.ORDERED, placement.order(id, RequestStatus.APPROVED));
    verify(supplier).place(id);
  }

  @Test
  void orderingADraftNeverCallsTheSupplier() {
    SupplierOrderPort supplier = mock(SupplierOrderPort.class);
    OrderPlacement placement = new OrderPlacement(policy, supplier);
    RequestId id = RequestId.newId();

    assertThrows(IllegalStateException.class, () -> placement.order(id, RequestStatus.DRAFT));
    verify(supplier, never()).place(id);
  }
}
