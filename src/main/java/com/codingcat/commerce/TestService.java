package com.codingcat.commerce;

import com.codingcat.commerce.customer.Customer;
import com.codingcat.commerce.customer.CustomerRepository;
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
