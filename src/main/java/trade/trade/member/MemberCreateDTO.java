package trade.trade.member;


import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class MemberCreateDTO {

    private MultipartFile userImage;
    private String email;
    private String password;
    private String name;
    private String description;
    private Integer phoneNumber;
    private String confirmPassword;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate joinDate;
}
