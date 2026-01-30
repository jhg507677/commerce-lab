package com.codingcat.commerce;

import com.codingcat.commerce.customer.Customer;
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
