package likelion.ylw.stats.statsResult;

import likelion.ylw.article.Article;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class StatsResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double pValue;

    private Boolean result = false;
    private Boolean male = false;
    private Boolean female = false;
    private Boolean age10 = false;
    private Boolean age20 = false;
    private Boolean age30 = false;
    private Boolean age40 = false;
    private Boolean ageOver50 = false;

    @OneToOne
    @JoinColumn(name = "article_id")
    private Article article;
}
