package com.codingcat.commerce.study;

import com.codingcat.commerce.domain.customer.Customer;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
  @Autowired TestService testService;

  @GetMapping("/test")
  public List<Customer> getAllMembers(){
    List<Customer> userList = testService.getAllMembers();
    return userList;
  }
}
