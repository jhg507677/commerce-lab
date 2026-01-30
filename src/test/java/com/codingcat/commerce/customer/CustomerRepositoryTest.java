package com.codingcat.commerce.customer;

import static org.assertj.core.api.Assertions.assertThat;

import com.codingcat.commerce.domain.customer.Customer;
import com.codingcat.commerce.domain.customer.CustomerRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest // 트랜잭션관리
class CustomerRepositoryTest {
  @Autowired
  CustomerRepository customerRepository;

  /*@Sql("/insert-customer.sql") 해당 애너테이션을 사용하면 테스트를 실행하기 전에 SQL 스크립트를 실행시킬 수 있음*/
  @Sql("/insert-customer.sql")
  @Test
  void getAllMembers(){
    // when
    List<Customer> customerList = customerRepository.findAll();

    assertThat(customerList.size()).isEqualTo(3);
  }


  @Sql("/insert-customer.sql")
  @Test
  void getMemberById(){
    // when
    Customer customer = customerRepository.findById(2L).get();
    assertThat(customer.getName()).isEqualTo("B");
  }


  @Sql("/insert-customer.sql") // 테스트를 실행하기 전에 SQL 스크립트 사용 가능
  @Test
  void getMemberByName(){
    // when
    Customer customer = customerRepository.findByName("C").get();
    assertThat(customer.getName()).isEqualTo("C");
  }

  @Test
  void saveMember(){
    // given
    Customer customer = new Customer(null, "D", "ddd");

    // when
    customerRepository.save(customer);

    //then
    assertThat(customer.getName()).isEqualTo(customerRepository.findByName("D").get().getName());
  }


  @Sql("/insert-customer.sql") // 테스트를 실행하기 전에 SQL 스크립트 사용 가능
  @Test
  void deleteMemberId(){
    customerRepository.deleteById(2L);
    assertThat(customerRepository.findById(2L).isEmpty()).isTrue();
  }

  @Sql("/insert-customer.sql") // 테스트를 실행하기 전에 SQL 스크립트 사용 가능
  @Test
  void deleteAll(){
    customerRepository.deleteAll();
    assertThat(customerRepository.findAll().size()).isZero();
  }

  @AfterEach
  public void cleanUp(){
    customerRepository.deleteAll();
  }

  @Sql("/insert-customer.sql") // 테스트를 실행하기 전에 SQL 스크립트 사용 가능
  @Test
  void update(){
    Customer customer = customerRepository.findById(2L).get();
    customer.changeName("BC");
    assertThat(customerRepository.findById(2L).get().getName()).isEqualTo("BC");
  }
}