package likelion.ylw.member;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

@Getter
public class MemberAccount extends User {
    private final Integer id;

    private final String memberImgPath;

    private final String nickname;

    public MemberAccount(Member member, Collection<? extends GrantedAuthority> authorities) {
        super(member.getMemberId(), member.getPassword(), authorities);
        this.id = member.getId();
        this.memberImgPath = member.getMemberImgPath();
        this.nickname = member.getNickname();
    }

    public String getMemberImgRedirectUrl() {
        return "/member/profile/img/" + getId() + "?random=" + UUID.randomUUID();
    }
}
