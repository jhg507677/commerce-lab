package com.codingcat.commerce.domain.order;

import com.codingcat.commerce.domain.BaseEntity;
import com.codingcat.commerce.domain.orderItem.OrderItem;
import com.codingcat.commerce.domain.product.Product;
import com.codingcat.commerce.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="orders")
public class Order extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "orders_idx")
  private Long idx;

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

  @Schema(description = "주문 상품 전체 합산 가격")
  private int totalPrice;

  @Schema(description = "주문 시간")
  private LocalDateTime orderedAt;

  @Schema(description = "주문한 유저")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="users_idx", nullable = false)
  private User user;


  // 주문 하나에 상품은 여러개
  // 주문 테이블 삭제시 주문 아이템들 모두 삭제
  // OrderItem이 연관관계 주인
  // 실제 FK 관리
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> orderItems = new ArrayList<>();

  // Order 생성시 가격 계산
  // 각각의 단위 테스트 필요
  // registeredDateTime 같은 경우 단위 테스트를 위해 파라미터로 빼야함
  public Order(List<Product> products, LocalDateTime orderedAt){
    this.orderStatus = OrderStatus.INIT;
    this.totalPrice = calculateTotalPrice(products);
    this.orderedAt = orderedAt;
    this.orderItems = products.stream()
      .map(product -> new OrderItem(this, product))
      .collect(Collectors.toList());
  }

  public static Order create(List<Product> products, LocalDateTime registeredDateTime) {
    return new Order(products, registeredDateTime);
  }

  private int calculateTotalPrice(List<Product> products) {
    return products.stream()
      .mapToInt(Product::getProductPrice)
      .sum();
  }
}
