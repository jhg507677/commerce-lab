package com.codingcat.commerce.module.security.token;

import com.codingcat.commerce.domain.BaseEntity;
import com.codingcat.commerce.domain.admin.Admin;
import com.codingcat.commerce.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// DB 기록용
@Table(name="token")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class LoginToken extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "token_idx", updatable = false)
  private Long idx;

  @Column(nullable = true) private String accessToken;
  @Column(nullable = true) private String refreshToken;

  @Schema(description = "토큰 만료시간")
  private Instant expiredDateTime;

  @Column(name = "user_idx", nullable = true)
  private Long userIdx;

  @Column(name = "admin_idx", nullable = true)
  private Long adminIdx;
//  @Schema(description = "인증을 시도한 사람")
//  @ManyToOne(fetch = FetchType.LAZY) // 여러 토큰이 한명의 유저에게
//  @JoinColumn(name="user_idx", nullable = true)
//  private User user;
//
//  @Schema(description = "인증을 시도한 사람")
//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name="admin_idx", nullable = true)
//  private Admin admin;
}
