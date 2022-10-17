package kitchenpos.eatinorders.tobe.domain.repository;

import kitchenpos.eatinorders.tobe.domain.model.EatInOrder;
import kitchenpos.eatinorders.tobe.domain.model.EatInOrderStatus;
import kitchenpos.eatinorders.tobe.domain.model.OrderTable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EatInOrderRepository {

    EatInOrder save(EatInOrder order);

    Optional<EatInOrder> findById(UUID id);

    List<EatInOrder> findAll();

    boolean existsByOrderTableAndOrderStatusNot(OrderTable orderTable, EatInOrderStatus status);
}
