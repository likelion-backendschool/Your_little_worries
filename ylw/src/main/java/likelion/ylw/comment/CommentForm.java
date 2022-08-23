package likelion.ylw.comment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CommentForm {

    @NotEmpty(message = "내용은 공백일 수 없습니다.")
    private String content;
}
