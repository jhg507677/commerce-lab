package com.codingcat.commerce.domain.stock;

import com.codingcat.commerce.domain.product.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
  List<Stock> findAllByProductIn(List<Product> products);
}
