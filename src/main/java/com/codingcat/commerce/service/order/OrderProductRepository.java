package com.codingcat.commerce.service.order;

import com.codingcat.commerce.service.orderItem.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderItem, Long> {

}
