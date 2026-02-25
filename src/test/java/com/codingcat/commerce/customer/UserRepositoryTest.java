package com.codingcat.commerce.customer;

import static org.assertj.core.api.Assertions.assertThat;

import com.codingcat.commerce.service.user.User;
import com.codingcat.commerce.service.user.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest // 트랜잭션관리
class UserRepositoryTest {
  @Autowired
  UserRepository userRepository;

  /*@Sql("/insert-customer.sql") 해당 애너테이션을 사용하면 테스트를 실행하기 전에 SQL 스크립트를 실행시킬 수 있음*/
  @Sql("/insert-customer.sql")
  @Test
  void getAllMembers(){
    // when
    List<User> userList = userRepository.findAll();

    assertThat(userList.size()).isEqualTo(3);
  }


  @Sql("/insert-customer.sql")
  @Test
  void getMemberById(){
    // when
    User user = userRepository.findById(2L).get();
    assertThat(user.getName()).isEqualTo("B");
  }


  @Sql("/insert-customer.sql") // 테스트를 실행하기 전에 SQL 스크립트 사용 가능
  @Test
  void getMemberByName(){
    // when
    User user = userRepository.findByName("C").get();
    assertThat(user.getName()).isEqualTo("C");
  }

  @Test
  void saveMember(){
    // given
    User user = User.createTestUser();

    // when
    userRepository.save(user);

    //then
    assertThat(user.getName()).isEqualTo(userRepository.findByName("D").get().getName());
  }


  @Sql("/insert-customer.sql") // 테스트를 실행하기 전에 SQL 스크립트 사용 가능
  @Test
  void deleteMemberId(){
    userRepository.deleteById(2L);
    assertThat(userRepository.findById(2L).isEmpty()).isTrue();
  }

  @Sql("/insert-customer.sql") // 테스트를 실행하기 전에 SQL 스크립트 사용 가능
  @Test
  void deleteAll(){
    userRepository.deleteAll();
    assertThat(userRepository.findAll().size()).isZero();
  }

  @AfterEach
  public void cleanUp(){
    userRepository.deleteAll();
  }

  @Sql("/insert-customer.sql") // 테스트를 실행하기 전에 SQL 스크립트 사용 가능
  @Test
  void update(){
    User user = userRepository.findById(2L).get();
    user.changeName("BC");
    assertThat(userRepository.findById(2L).get().getName()).isEqualTo("BC");
  }
}