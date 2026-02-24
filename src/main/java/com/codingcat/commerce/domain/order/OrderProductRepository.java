package com.codingcat.commerce.domain.order;

import com.codingcat.commerce.domain.orderItem.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderItem, Long> {

}
