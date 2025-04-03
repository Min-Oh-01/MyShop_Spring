package trade.trade.Product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@ToString

@EntityListeners(AuditingEntityListener.class)

@Getter
@Setter
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 상품 이미지
    @Column(columnDefinition = "TEXT")
    private String productImage;

    // 상품 이름
    @Column(nullable = false)
    private String productName;

    // 상품 개요
    @Column(length = 200)
    private String productOutline;

    // 상품 가격
    @Column(nullable = false)
    private Float productPrice;

    // 게시글을 올린 날짜
    @CreatedDate
    private LocalDate productDate;

    // 상품 사용 시작 기간
    @Column
    private LocalDate startDate;

    // 상품 사용 끝난 기간
    @Column
    private LocalDate endDate;
}
