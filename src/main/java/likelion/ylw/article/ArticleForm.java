package likelion.ylw.article;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ArticleForm {
    @NotEmpty(message = "제목은 공백일 수 없습니다.")
    @Size(max = 200)
    private String title;

    @NotEmpty(message = "내용은 공백일 수 없습니다.")
    private String content;

    private String author;

    private String[] items;

//    private Integer categoryId;
}
