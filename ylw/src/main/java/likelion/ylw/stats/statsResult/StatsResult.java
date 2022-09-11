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
    private Boolean compareGender;
    private Boolean compare10And20;
    private Boolean compare10And30;
    private Boolean compare10And40;
    private Boolean compare20And30;
    private Boolean compare20And40;
    private Boolean compare30And40;

    @OneToOne
    private Article article;
}
