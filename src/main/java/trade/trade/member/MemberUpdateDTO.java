package trade.trade.member;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class MemberUpdateDTO {
    private Long id;
    private MultipartFile userImage;
    private String originalImage;
    private String email;
    private String password;
    private String name;
    private String description;
    private Integer phoneNumber;
    private LocalDate joinDate;
}
