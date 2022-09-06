package likelion.ylw.article;

import likelion.ylw.stats.StatsCollection;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class ArticleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;

    @ManyToOne
    private Article article;

    @OneToMany
    private List<StatsCollection> statsCollection;
}
