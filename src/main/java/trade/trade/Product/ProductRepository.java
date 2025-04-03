package trade.trade.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Page<ProductEntity> findByProductNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<ProductEntity> findAll(Pageable pageable);


    // 가격 분류해서 보내기
    // 만원 미만
    Page<ProductEntity> findByProductPriceLessThan(Float price, Pageable pageable);
    // 만원 이상 5만원 미만
    Page<ProductEntity> findByProductPriceBetween(Float minPrice, Float maxPrice, Pageable pageable);
    // 5만원 이상
    Page<ProductEntity> findByProductPriceGreaterThan(Float price, Pageable pageable);
}
