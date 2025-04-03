package trade.trade.member;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "UsedTradeMember")

@EntityListeners(AuditingEntityListener.class)

@ToString
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 회원 아이디, 비밀번호, 가입날짜, 이름, 자기소개, 휴대폰 번호

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private Integer phoneNumber;

    @CreatedDate
    private LocalDate joinDate;

    @Column
    private String userImage;
}
