package likelion.ylw.comment;

import likelion.ylw.article.Article;
import likelion.ylw.comment.vote.CommentVote;
import likelion.ylw.member.Member;
import likelion.ylw.util.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 *  추후 builder 추가
 *  voter Entity 따로 추가 할지 말지
 */
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

    @ManyToOne
    private Article article;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = true)
    private Member author;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    Set<CommentVote> votes = new HashSet<>();

    @ColumnDefault("false")
    private boolean isBlind;
}
