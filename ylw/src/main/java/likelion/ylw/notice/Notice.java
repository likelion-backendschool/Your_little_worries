package likelion.ylw.notice;

import likelion.ylw.member.Member;
import likelion.ylw.util.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Notice extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Member author;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Integer viewCount;
}
