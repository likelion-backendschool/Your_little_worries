package likelion.ylw.stats;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class StatsCollectionForm {

    private Integer articleItemId;

    @NotNull(message = "나이를 입력해주세요.")
    private Integer age;

    @NotEmpty(message = "성별을 입력해주세요.")
    private String gender;
}
