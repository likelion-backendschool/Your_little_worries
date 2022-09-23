package likelion.ylw.comment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class NonMemberCommentForm {

    @NotBlank(message = "닉네임을 입력해주세요")
    @Size(max = 20, message = "닉네임을 10글자 아래로 입력해주세요.")
    private String tempNickname;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(max = 20)
    private String tempPassword;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}