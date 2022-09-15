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

    private Boolean result;
    private Boolean male;
    private Boolean female;
    private Boolean age10;
    private Boolean age20;
    private Boolean age30;
    private Boolean age40;
    private Boolean ageOver50;

    @OneToOne
    private Article article;
}
