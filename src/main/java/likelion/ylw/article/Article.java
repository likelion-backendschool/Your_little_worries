package likelion.ylw.article;

import likelion.ylw.article.recommend.ArticleRecommend;
import likelion.ylw.category.Category;
import likelion.ylw.comment.vote.CommentVote;
import likelion.ylw.member.Member;
import likelion.ylw.stats.StatsCollection;
import likelion.ylw.util.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@DynamicInsert
public class Article extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Member author;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition="BIGINT(20) default 0")
    private Long viewCount;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    Set<ArticleRecommend> recommends = new HashSet<>();

    @ColumnDefault("false")
    private boolean isBlind;

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<ArticleItem> articleItemList;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<StatsCollection> statsCollectionList;
}
