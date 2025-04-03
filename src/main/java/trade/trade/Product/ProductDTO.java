package trade.trade.Product;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProductDTO { // 계층간 데이터 전달

    private Long id;
    private Float productPrice;
    private LocalDate productDate;
    private String productImage;
    private String productName;
    private String productOutline;
    private LocalDate startDate;
    private LocalDate endDate;
}
