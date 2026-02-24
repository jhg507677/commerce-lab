package com.codingcat.commerce.domain.stock;

import com.codingcat.commerce.domain.BaseEntity;
import com.codingcat.commerce.domain.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idx;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_idx")
  private Product product;

  private int quantity;

  private boolean unlimited; // true이면 무한 재고

  @Builder
  public Stock(Product product, int quantity, boolean unlimited) {
    this.product = product;
    this.quantity = quantity;
    this.unlimited = unlimited;
  }
  public static void create(List<Product> savedProducts) {
  }
}