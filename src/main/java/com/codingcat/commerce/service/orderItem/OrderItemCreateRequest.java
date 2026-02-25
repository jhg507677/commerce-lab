package com.codingcat.commerce.service.orderItem;

import com.codingcat.commerce.module.model.BaseEntity;
import com.codingcat.commerce.service.order.Order;
import com.codingcat.commerce.service.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "주문 상품 요청")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemCreateRequest{
  @Positive
  private int productIdx;
}
