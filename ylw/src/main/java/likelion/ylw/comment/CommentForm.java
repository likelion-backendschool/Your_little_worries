package likelion.ylw.comment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CommentForm {

    @Size(max = 10, message = "닉네임을 10글자 아래로 입력해주세요.")
    private String tempNickname;

    @Size(max = 20)
    private String tempPassword;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}
