package com.codingcat.commerce.api.service.order.dto;

import com.codingcat.commerce.api.service.product.dto.ProductResponse;
import com.codingcat.commerce.domain.order.Order;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderResponse {
  private Long idx;
  private int totalPrice;
  private LocalDateTime registeredDateTime;
  private List<ProductResponse> products;

  @Builder
  public OrderResponse(Long idx, int totalPrice, LocalDateTime registeredDateTime,
    List<ProductResponse> products) {
    this.idx = idx;
    this.totalPrice = totalPrice;
    this.registeredDateTime = registeredDateTime;
    this.products = products;
  }

  public static OrderResponse toOrder(Order order) {
    return OrderResponse.builder()
      .idx(order.getIdx())
      .totalPrice(order.getTotalPrice())
      .registeredDateTime(order.getRegisteredDateTime())

      .products(order.getOrderProducts().stream()
        .map(product -> ProductResponse.of(product.getProduct()))
        .collect(Collectors.toList())
      )
    .build();
  }
}
