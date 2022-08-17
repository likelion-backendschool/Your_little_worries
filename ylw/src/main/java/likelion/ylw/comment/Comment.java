package likelion.ylw.comment;

import likelion.ylw.article.Article;
import likelion.ylw.member.Member;
import likelion.ylw.util.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String tempNickname;

    private String tempPassword;

    @ManyToMany
    Set<Member> voter;

    @ManyToOne
    private Article article;

    @ManyToOne
//    @Column
    @JoinColumn(name = "member_id", nullable = true)
    private Member author;

    @ColumnDefault("false")
    private boolean isBlind;
}
