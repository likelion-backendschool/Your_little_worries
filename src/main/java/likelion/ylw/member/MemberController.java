package likelion.ylw.member;

import likelion.ylw.article.Article;
import likelion.ylw.article.ArticleService;
import likelion.ylw.member.mail.MailForm;
import likelion.ylw.member.mail.NotFoundEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final ArticleService articleService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    // 회원가입 폼 (memberId, 닉네임, 비밀번호, 프로필 이미지 입력)
    @GetMapping("/signup")
    public String signup(MemberCreateForm memberCreateForm) {
        return "member/member_signup_form";
    }

    // 회원가입 처리 (루트로 이동)
    @PostMapping("/signup")
    public String signup(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/member_signup_form";
        }
        if (!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordIncorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "member/member_signup_form";
        }
        try {
            memberService.create(memberCreateForm.getMemberId(),
                    memberCreateForm.getPassword1(), memberCreateForm.getEmail(), memberCreateForm.getNickname());
        } catch (SignupEmailDuplicatedException e) {
            bindingResult.reject("signupEmailDuplicated", e.getMessage());
            return "member/member_signup_form";
        } catch (SignupMemberIdDuplicatedException e) {
            bindingResult.reject("signupUsernameDuplicated", e.getMessage());
            return "member/member_signup_form";
        }
        return "redirect:/";
    }

    // 로그인 폼
    @GetMapping("/login")
    public String login(HttpServletRequest request) {

        String uri = request.getHeader("Referer");
        if (uri != null && !uri.contains("/login") && !uri.contains("/mail") && !uri.contains("/member") ) {
            request.getSession().setAttribute("prevPage", uri);
        }

        return "member/member_login_form";
    }

    // 아이디 찾기 폼(이메일 입력)
    @GetMapping("/find/id")
    public String find_id(MailForm mailForm) {
        return "member/member_find_id_form";
    }

    // 아이디 찾기 처리 (아이디 찾기 완료 페이지로 이동)
    @PostMapping("/find/id")
    public String find_id(Model model, @Valid MailForm mailForm, BindingResult bindingResult) {
        try {
            Member member = memberService.findByEmail(mailForm.getEmail());
            model.addAttribute("member", member);
            return "member/member_find_id_complete";
        } catch(NotFoundEmailException e) {
            bindingResult.reject("notFoundEmail", e.getMessage());
            return "member/member_find_id_form";
        }
    }

    // 마이페이지
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage")
    public String my_page(Principal principal, Model model) {
        List<Member> members = memberService.findAll();
        Member member = memberService.findByMemberId(principal.getName());
        memberService.evalLevel(member);
        memberService.evalTotalScore(member);
        List<Article> myArticles = articleService.findByAuthor(member);
        model.addAttribute("members", members);
        model.addAttribute("member",member);
        model.addAttribute("myArticles", myArticles);
        return "member/member_myPage";
    }

    @PostMapping("/myPage")
    public String my_page(Principal principal, Model model, MultipartFile memberImg) {
        String memberId = principal.getName();
        Member member = memberService.findByMemberId(memberId);
        memberService.updateImage(member, memberImg);
        model.addAttribute("member", member);
        return "member/member_myPage";
    }

    // 회원정보 수정 폼 (닉네임, 비밀번호 변경가능)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit/profile")
    public String my_page_edit(MemberUpdateForm memberUpdateForm, Principal principal, Model model) {
        Member member = memberService.findByMemberId(principal.getName());
        model.addAttribute("member", member);
        return "member/member_edit_form";
    }

    // 회원 정보 수정 처리 (마이페이지로 이동)
    @PostMapping("/edit/profile")
    public String my_page_edit(@Valid MemberUpdateForm memberUpdateForm, BindingResult bindingResult, Model model, Principal principal) {
        System.out.println("---------------------");
        System.out.println("principal.getName : " + principal.getName());
        System.out.println("---------------------");

        Member member = memberService.findByMemberId(principal.getName());

        System.out.println("---------------------");
        System.out.println("member : " + member);
        System.out.println("---------------------");

        if (!memberUpdateForm.getPassword1().equals(memberUpdateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordIncorrect",
                    "2개의 패스워드가 일치하지 않습니다.");

            model.addAttribute("member", member);
            return "member/member_edit_form";
        }
        memberService.update(memberUpdateForm, member);
        return "redirect:/member/myPage";
    }

    //회원 탈퇴 폼 (비밀번호 입력 시 탈퇴)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete")
    public String delete_account(MemberDeleteForm memberDeleteForm) {
        return "member/member_delete_form";
    }

    //회원 탈퇴 처리 (탈퇴 완료 페이지로 이동)
    @PostMapping("/delete")
    public String delete_account(@Valid MemberDeleteForm memberDeleteForm, BindingResult bindingResult, Principal principal) {
        String memberId = principal.getName();
        Member member = memberService.findByMemberId(memberId);
        System.out.println("--------------------");
        System.out.println("memberDeleteForm.getPassword : " + memberDeleteForm.getPassword());
        System.out.println("--------------------");

        System.out.println("--------------------");
        System.out.println("member.getPassword : " + member.getPassword());
        System.out.println("--------------------");
        boolean matches = !passwordEncoder.matches(member.getPassword(), memberDeleteForm.getPassword());
        System.out.println("--------------------");
        System.out.println("boolean passwordEncoder.matches : " + matches);
        System.out.println("--------------------");

        if (!passwordEncoder.matches(memberDeleteForm.getPassword(), member.getPassword())) {
            System.out.println("--------------------");
            System.out.println("패스워드가 일치하지 않습니다.");
            System.out.println("--------------------");
            bindingResult.rejectValue("password", "passwordNotMatched",
                    "패스워드가 일치하지 않습니다.");
            return "member/member_delete_form";
        }
        System.out.println("--------------------");
        System.out.println("패스워드가 일치합니다.");
        System.out.println("--------------------");
        System.out.println("--------------------");
        System.out.println("delete 하기전");
        System.out.println("--------------------");
        memberService.delete(memberDeleteForm, memberId);
        System.out.println("--------------------");
        System.out.println("delete 하고난 후");
        System.out.println("--------------------");
        return "member/member_delete_complete";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/rank")
    public String search_rank(Principal principal, Model model) {
        String memberId = principal.getName();
        Member member = memberService.findByMemberId(memberId);
        List<Member> orderedMembers = memberService.getRankingList();
        memberService.evalRank();
        model.addAttribute("orderedMembers", orderedMembers);
        model.addAttribute("member", member);

        return "member/member_ranking";
    }

    @GetMapping("/profile/img/{id}")
    public String showProfileImg(@PathVariable Integer id) {
        return "redirect:" + memberRepository.findById(id).orElse(null).getMemberImgUrl();
    }
}