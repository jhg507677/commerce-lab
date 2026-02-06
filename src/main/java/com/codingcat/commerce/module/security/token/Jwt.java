package com.codingcat.commerce.module.security.token;

import com.codingcat.commerce.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.security.Timestamp;

// DB 기록용
@Entity
public class Jwt  extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "jwt_idx", updatable = false)
  private Long idx;

  @Enumerated(EnumType.STRING)
  private TokenType tokenType;
  private String token;
  private Timestamp expireAt;
  private Long userIdx;
  private Long AdminIdx;
}
