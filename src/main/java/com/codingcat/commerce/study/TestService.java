package com.codingcat.commerce.study;

import com.codingcat.commerce.domain.customer.Customer;
import com.codingcat.commerce.domain.customer.CustomerRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

  @Autowired
  CustomerRepository userRepository;

  public List<Customer> getAllMembers() {
    return userRepository.findAll();
  }
}
