package com.codingcat.commerce.module.security;

import com.codingcat.commerce.domain.user.User;
import com.codingcat.commerce.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUserId(username).orElse(null);
    if(user == null) throw new UsernameNotFoundException("존재하지 않는 아이디입니다.");
    return UserPrincipal.from(user);
  }
}
