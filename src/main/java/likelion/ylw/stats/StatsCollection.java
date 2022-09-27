package likelion.ylw.stats;

import likelion.ylw.article.Article;
import likelion.ylw.article.ArticleItem;
import likelion.ylw.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
public class StatsCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private ArticleItem articleItem;

    private Integer age;

    private String gender;

    @ManyToOne
//    @Column(nullable = true)
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;

    private String IP;
    //id articleItem age gender member  IP
    //1     1        20   male    qwe   123,213,112,211
    //2     1        21   female  asd   111,132,131,213
    //3     1        22   male    zxc   222,333,111,222
    //4     1        23   male    (null)  111,222,333,444

    @ManyToOne
    private Article article;
}
