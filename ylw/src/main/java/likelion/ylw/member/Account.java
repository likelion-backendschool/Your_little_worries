package likelion.ylw.member;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class Account extends User {

    private final String memberImgPath;

    public Account(Member member, Collection<? extends GrantedAuthority> authorities) {
        super(member.getMemberId(), member.getPassword(), authorities);
        this.memberImgPath = member.getMemberImgPath();
    }
}
