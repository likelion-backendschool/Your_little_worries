package likelion.ylw.notice;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class NoticeForm {
    @NotEmpty(message="제목은 필수항목입니다.")
    @Size(max=255)
    private String title;

    @NotEmpty(message="내용은 필수항목입니다.")
    private String content;
}
