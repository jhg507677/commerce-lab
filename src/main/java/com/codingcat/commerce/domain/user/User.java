package com.codingcat.commerce.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity // 해당 객체를 JPA관리하는 엔티티로 지정, 즉 Customer 클래스와 실제 customer 테이블을 매핑, 이름을 다르게 하고 싶다면 name 속성 사용
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_idx", updatable = false)
  private Long idx;

  @Column(nullable = false)
  private String id;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String password;

  @Column(nullable = true)
  private String role;


  /*
  서비스코드에서 업데이트 기능을 사용할려면 서비스 메서드에 반드시 @Transactional을 붙여야함
  */
  public void changeName(String name){
    this.name = name;
  }
}
