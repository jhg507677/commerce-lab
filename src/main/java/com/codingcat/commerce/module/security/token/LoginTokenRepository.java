package com.codingcat.commerce.module.security.token;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginTokenRepository extends JpaRepository<LoginToken, Long> {

}
