package likelion.ylw.stats;

import likelion.ylw.article.Article;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class StatsResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double pValue;

    private Double sigLevel;

    private Boolean result;

    @ManyToOne
    private Article article;
}
