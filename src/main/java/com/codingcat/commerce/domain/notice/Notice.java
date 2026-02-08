package com.codingcat.commerce.domain.notice;

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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="notices")
public class Notice {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="notice_idx", updatable = false)
  private Long idx;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  @Schema(description = "작성한 관리자")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="admin_idx", nullable = true)
  private Admin admin;

  // 생성자 위에 입력하면 빌더 패턴 방식으로 객체를 생성하는 것이 가능함
  @Builder
  public Notice(String title, String content){
    this.title=title;
    this.content = content;
  }

  // 엔티티에 업데이트 ㅁ여시
  public void update(String title, String content){
    this.title = title;
    this.content = content;
  }
}
