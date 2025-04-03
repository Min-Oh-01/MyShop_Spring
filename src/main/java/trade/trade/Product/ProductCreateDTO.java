package trade.trade.Product;


import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class ProductCreateDTO {
    private MultipartFile image;
    private String name;
    private Float price;
    private String productOutline;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate; //  ISO 표준에 맞춰 시간을 표시!
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate productDate;
}
