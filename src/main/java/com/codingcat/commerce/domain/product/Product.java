package com.codingcat.commerce.domain.product;

import com.codingcat.commerce.domain.admin.Admin;
import io.swagger.v3.oas.annotations.media.Schema;
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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="product")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="product_idx", updatable = false)
  private Long idx;

  @Schema(description = "상품코드")
  private String code;

  // Enum 값을 DB에 “문자열”로 저장해 줌 -> HANDMADE
  @Enumerated(EnumType.STRING)
  private ProductType type;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String content;

  @Enumerated(EnumType.STRING)
  private ProductSellingStatus sellingStatus;

  // @Column(nullable = false)
  @Schema(description = "등록한 관리자")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="admin_id")
  private Admin admin;

  private Integer price;


  @Builder
  public Product(String name, String content){
    this.name=name;
    this.content = content;
  }

  public Product(String name, String content, Admin admin, String code) {
    this.name = name;
    this.content = content;
    this.admin = admin;
    this.code = code;
  }
}
