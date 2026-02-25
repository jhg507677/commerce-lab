package com.codingcat.commerce.study;

import com.codingcat.commerce.service.user.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
  @Autowired TestService testService;

  @GetMapping("/test")
  public List<User> getAllMembers(){
    List<User> userList = testService.getAllMembers();
    return userList;
  }
}
