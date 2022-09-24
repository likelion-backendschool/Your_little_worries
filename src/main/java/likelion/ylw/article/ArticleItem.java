package likelion.ylw.article;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


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

    private Integer total = 0;
    private Integer maleTotal = 0;
    private Integer femaleTotal = 0;
    private Integer total10 = 0;
    private Integer total20 = 0;
    private Integer total30 = 0;
    private Integer total40 = 0;
    private Integer totalOver50 = 0;

}
