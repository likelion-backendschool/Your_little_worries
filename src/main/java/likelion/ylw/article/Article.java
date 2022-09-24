package likelion.ylw.article;

import likelion.ylw.category.Category;
import likelion.ylw.member.Member;
import likelion.ylw.member.Role;
import likelion.ylw.util.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
public class Article extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Member author;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Integer viewCount;

    @ManyToMany
    Set<Member> voter;

    @ColumnDefault("false")
    private boolean isBlind;

    @ManyToOne
    private Category category;

}
