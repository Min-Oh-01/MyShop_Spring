package trade.trade.Product;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class ProductUpdateDTO {
    private Long id;
    private MultipartFile image;
    private String originalImage;
    private String productName;
    private Float productPrice;
    private String productOutline;
    private LocalDate productDate;
    private LocalDate startDate;
    private LocalDate endDate;
}
