package kitchenpos.deliveryorders;

import kitchenpos.common.AbstractOrder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("DELIVERY")
public class DeliveryOrder extends AbstractOrder {

    @Enumerated(EnumType.STRING)
    private DeliveryOrderStatus status;

    @Column(name = "delivery_address")
    private String deliveryAddress;
}
