package com.codingcat.commerce.domain.order;


import com.codingcat.commerce.domain.order.dto.OrderCreateRequest;
import com.codingcat.commerce.domain.order.dto.OrderResponse;
import com.codingcat.commerce.domain.product.Product;
import com.codingcat.commerce.domain.product.ProductRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;

  // 상품 주문
  public OrderResponse createOrder(
    OrderCreateRequest request, LocalDateTime registeredDateTime
  ) {
    // 상품 가져오기
    List<String> requestProductNumbers = request.getProductNumbers();

    // DB에서 상품 조회
    // TODO : 조인시 남은 재고 차량 여부도 같이 가져와야함
    List<Product> dbProducts = productRepository.findAllByCodeIn(requestProductNumbers);

    // TODO : 재고 차감 필요

    List<Product> orderedProducts = findProductByDuplicateProductNumber(
      dbProducts, requestProductNumbers);

    Order order = Order.create(orderedProducts, registeredDateTime);
    Order savedOrder = orderRepository.save(order);
    return OrderResponse.toOrder(savedOrder);
  }

  private static List<Product> findProductByDuplicateProductNumber(List<Product> dbProducts,
    List<String> requestProductNumbers) {
    // 반복문으로 할 경우
//    List<Product> orderProduct = new ArrayList<>();
//    if(dbProducts.size() != productNumbers.size()){
//
//      for (String productNumber : productNumbers) {
//        for (Product product : dbProducts) {
//          if (product.getProductNumber().equals(productNumber)) {
//            orderProduct.add(product);
//            break;
//          }
//        }
//      }
//    }


    // DB 상품들을 productNumber 기준으로 Map으로 변환
    // "A001"	[Product("A001", 1000), Product("A001", 1000)]
    // "B002"	[Product("B002", 1500)]
    Map<String, List<Product>> dbProductMap = dbProducts.stream()
      .collect(Collectors.groupingBy(Product::getCode));

    // 요청한 상품들을 DB 상품들과 비교해서 Map으로 반환
    // [Product("A001", 1000),Product("A001", 1000),Product("B002", 1500)]
    List<Product> orderedProducts = requestProductNumbers.stream()
      .flatMap(productNumber -> {
        // DB에서 해당 상품 주문 번호로 상품들 가져와서
        List<Product> productList = dbProductMap.get(productNumber);

        // 사용자가 요청한 상품 번호와 DB의 상품 번호중 맞지 않는 경우가 있을때
        if (productList == null) {
          throw new IllegalArgumentException(
            "존재하지 않는 상품 번호입니다: " + productNumber
          );
        }
        return productList.stream();
      })
      .collect(Collectors.toList());
    return orderedProducts;
  }

}
