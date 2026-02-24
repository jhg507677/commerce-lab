package com.codingcat.commerce.domain.orderItem;

import com.codingcat.commerce.module.model.BaseEntity;
import com.codingcat.commerce.domain.order.Order;
import com.codingcat.commerce.domain.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "주문 상품")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderItem extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_item_idx")
  private Long idx;

  @Schema(description = "주문")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="orders_idx", nullable = false)
  private Order order;

  @Schema(description = "주문 상품")
  @ManyToOne(fetch = FetchType.LAZY)
  @Column(name="product_idx", nullable = false)
  private Product product;

  @Schema(description = "주문 상품 가격")
  @Column(nullable = false)
  private Integer OrderItemPrice;

  @Column(nullable = false)
  private Integer count = 1;

  public OrderItem(Order order, Product product) {
    this.order = order;
    this.product = product;
  }
}
