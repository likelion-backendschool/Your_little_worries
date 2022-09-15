package likelion.ylw.article;

import likelion.ylw.article.recommend.ArticleRecommend;
import likelion.ylw.category.Category;
import likelion.ylw.comment.vote.CommentVote;
import likelion.ylw.member.Member;
import likelion.ylw.util.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
public class Article extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Member author;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Long viewCount;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    Set<ArticleRecommend> recommends = new HashSet<>();

    @ColumnDefault("false")
    private boolean isBlind;

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<ArticleItem> articleItemList;
}
