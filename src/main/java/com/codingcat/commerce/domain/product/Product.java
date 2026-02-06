package com.codingcat.commerce.domain.product;

import com.codingcat.commerce.domain.admin.Admin;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String content;

  //  @Column(nullable = false)
  @Schema(description = "등록한 관리자")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="admin_id")
  private Admin admin;

  // 추후 oneToMany로 확장 필요
  @Schema(description = "상품코드")
  private String code;

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
