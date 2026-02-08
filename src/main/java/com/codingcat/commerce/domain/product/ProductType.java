package com.codingcat.commerce.domain.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductType {
  LOL("리그 오브 레전드")
  ,OVERWATCH("오버워치")
  ,MINECRAFT("마인크래프트")
  ;

  private final String desc;
}
