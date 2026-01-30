package com.codingcat.commerce;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
RequestParam : 쿼리 스트링
PathVariable : URL 경로, 리소스 식별자
*/
@RestController
public class QuizController {
  @GetMapping("quiz")
  public ResponseEntity<String> quiz(
    @RequestParam("code") int code
  ){
    switch (code){
      case 1 :
        return ResponseEntity.created(null).body("Created!");
      case 2 :
        return ResponseEntity.badRequest().body("Bad Request!");
      default :
        return ResponseEntity.ok().body("OK!");
    }
  }

  @PostMapping("quiz")
  public ResponseEntity<String> quiz2(
    @RequestBody Code code
  ){
    switch (code.value()){
      case 1 :
        return ResponseEntity.status(403).body("Forbidden!");
      default :
        return ResponseEntity.ok().body("OK!");
    }
  }

  record Code(int value){};
}

/*
record란?
데이터 전달을 목적으로 하는 객체를 더 빠르고 간편하게 만들기 위한 기능으로 레코드를 사용하면 필드, 생성자, 게터, equals, hashCode, toString 메서드 등을 자동으로 생성합니다.
다만 불변으로 생성자를 통해 처음 값을 입력받을때빼고는 값 변경이 불가함
*/
