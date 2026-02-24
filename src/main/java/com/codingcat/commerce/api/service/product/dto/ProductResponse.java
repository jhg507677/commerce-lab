package com.codingcat.commerce.api.service.product.dto;

import com.codingcat.commerce.domain.product.Product;
import com.codingcat.commerce.domain.product.ProductSellingStatus;
import com.codingcat.commerce.domain.product.ProductType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductResponse {
  private Long idx;
  private String productNumber;
  private ProductType type;
  private ProductSellingStatus sellingStatus;
  private String name;
  private int price;

  public static ProductResponse of(Product item) {
    return ProductResponse.builder()
      .idx(item.getIdx())
      .productNumber(item.getCode())
      .type(item.getType())
      .sellingStatus(item.getSellingStatus())
      .name(item.getName())
      .price(item.getProductPrice())
      .build();
  }

  @Builder
  private ProductResponse(Long idx, String productNumber, ProductType type,
    ProductSellingStatus sellingStatus, String name, int price) {
    this.idx = idx;
    this.productNumber = productNumber;
    this.type = type;
    this.sellingStatus = sellingStatus;
    this.name = name;
    this.price = price;
  }
}
