package com.codingcat.commerce.domain.user;

import com.codingcat.commerce.module.security.token.LoginToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByName(String name);

  Optional<User> findByUserId(User entity);

}