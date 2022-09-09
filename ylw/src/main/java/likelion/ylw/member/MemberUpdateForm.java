package likelion.ylw.member;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class MemberUpdateForm {
    private String password1;

    private String password2;

    private String nickname;

    private MultipartFile memberImg;
}
