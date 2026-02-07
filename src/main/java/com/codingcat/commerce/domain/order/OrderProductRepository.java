package com.codingcat.commerce.domain.order;

import com.codingcat.commerce.domain.orderproduct.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}
