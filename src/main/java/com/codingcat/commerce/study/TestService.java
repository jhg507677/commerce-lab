package com.codingcat.commerce.study;

import com.codingcat.commerce.domain.user.User;
import com.codingcat.commerce.domain.user.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

  @Autowired
  UserRepository userRepository;

  public List<User> getAllMembers() {
    return userRepository.findAll();
  }
}
