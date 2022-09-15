package likelion.ylw.stats.statsItemResult;

import likelion.ylw.article.Article;
import likelion.ylw.article.ArticleItem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class StatsItemResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Article article;

    @OneToOne
    private ArticleItem articleItem;

    private Integer total = 0;
    private Integer maleTotal = 0;
    private Integer femaleTotal = 0;
    private Integer total10 = 0;
    private Integer total20 = 0;
    private Integer total30 = 0;
    private Integer total40 = 0;
    private Integer totalOver50 = 0;
}