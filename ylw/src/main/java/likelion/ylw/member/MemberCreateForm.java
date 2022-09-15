package likelion.ylw.member;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class MemberCreateForm {
    @Size(min = 3, max = 25, message = "사용자ID는 3자이상, 25자 이하로 입력해주세요.")
    @NotEmpty(message = "사용자ID는 필수항목입니다.")
    private String memberId;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String password2;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email(message = "올바른 이메일 형식으로 입력해주세요.")
    private String email;

    @Size(min = 3, max = 25, message = "닉네임은 3자이상, 25자 이하로 입력해주세요.")
    @NotEmpty(message = "닉네임은 필수항목입니다.")
    private String nickname;

    private Integer score;
}
