package likelion.ylw.member;

import likelion.ylw.util.AppConfig;
import likelion.ylw.util.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

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

    @Column
    @ColumnDefault("0")
    private Integer enrollScore;

    @Column
    @ColumnDefault("0")
    private Integer participateScore;

    @Column
    @ColumnDefault("0")
    private Integer popularVoteScore;

    @Column
    @ColumnDefault("0")
    private Integer currentRank;

    @Column
    @ColumnDefault("1")
    private Integer currentLevel;

    private String memberImgPath;

    public void removeProfileImgOnStorage() {
        if (memberImgPath == null || memberImgPath.trim().length() == 0) return;

        String profileImgPath = getProfileImgPath();

        new File(profileImgPath).delete();
    }

    private String getProfileImgPath() {

        return AppConfig.GET_FILE_DIR_PATH + "/" + memberImgPath;
    }

    public String getMemberImgUrl() {
        if (memberImgPath == null) return null;

        return "/gen/" + memberImgPath;
    }

}
