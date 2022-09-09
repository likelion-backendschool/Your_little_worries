package likelion.ylw.member;

import likelion.ylw.util.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@DynamicInsert
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, nullable = false, unique = true)
    private String memberId;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column
    @ColumnDefault("0")
    private Integer score;

    @Column
    @ColumnDefault("0")
    private Integer enrollCount;

    @Column
    @ColumnDefault("0")
    private Integer participateCount;

    @Column
    @ColumnDefault("0")
    private Integer popularVoteCount;

    private String memberImgPath;
//    private LocalDateTime createdDate;

//    @Enumerated(EnumType.STRING)
//    private Role role;
}
