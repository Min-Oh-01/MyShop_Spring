package trade.trade.member;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MemberDTO {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String description;
    private Integer phoneNumber;
    private LocalDate joinDate;
    private String userImage;

}
