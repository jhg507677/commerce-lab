package com.codingcat.commerce.domain.product;

import com.codingcat.commerce.service.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/*
@SpringBootTest : 애플리케이션 전체 컨텍스트를 로딩하는 통합 테스트용 어노테이션
@DataJpaTest     : JPA 관련 빈만 로딩하여 Repository 계층을 빠르게 검증하는 테스트용 어노테이션
*/
@SpringBootTest
//@DataJpaTest
class ProductRepositoryTest {
  @Autowired
  ProductRepository productRepository;

  @DisplayName("")
  @Test
  void findAllBySelling(){
    // given


    // when


    // then


  }
}