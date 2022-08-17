package likelion.ylw.article;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class ArticleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;

    @ManyToOne
    private Article article;
}
