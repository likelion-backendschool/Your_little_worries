package likelion.ylw.member;

import likelion.ylw.member.Mail.MailForm;
import likelion.ylw.member.Mail.NotFoundEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String signup(MemberCreateForm memberCreateForm) {
        return "member/signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/signup_form";
        }

        if (!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordIncorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "member/signup_form";
        }

        try {
            memberService.create(memberCreateForm.getMemberId(),
                    memberCreateForm.getPassword1(), memberCreateForm.getEmail(), memberCreateForm.getNickname());
        } catch (SignupEmailDuplicatedException e) {
            bindingResult.reject("signupEmailDuplicated", e.getMessage());
            return "member/signup_form";
        } catch (SignupMemberIdDuplicatedException e) {
            bindingResult.reject("signupUsernameDuplicated", e.getMessage());
            return "member/signup_form";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "member/login_form";
    }

    @GetMapping("/findID")
    public String find_id(MailForm mailForm) {

        return "member/findID_form";
    }

    @PostMapping("/findID")
    public String find_id_post(Model model, @Valid MailForm mailForm, BindingResult bindingResult) {
        try {
            Member member = memberService.findByEmail(mailForm.getEmail());
            model.addAttribute("member", member);
            return "member/findID_result";
        } catch(NotFoundEmailException e) {
            bindingResult.reject("notFoundEmail", e.getMessage());
            return "member/findID_form";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage")
    public String my_page(Principal principal, Model model) {
        System.out.println("principal getName(): " + principal.getName());
        Member member = memberService.getMemberId(principal.getName());
        model.addAttribute("member", member);
        return "member/myPage_form";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/edit")
    public String my_page_edit(MemberUpdateForm memberUpdateForm, Principal principal, Model model) {

        Member member = memberService.getMemberId(principal.getName());

        model.addAttribute("member", member);
        return "member/editProfile_form";
    }

    @PostMapping("/profile/edit")
    public String my_page_edit(@Valid MemberUpdateForm memberUpdateForm, BindingResult bindingResult, Model model, Principal principal) {
        System.out.println("---------------------");
        System.out.println("principal.getName : " + principal.getName());
        System.out.println("---------------------");

        Member member = memberService.getMemberId(principal.getName());

        System.out.println("---------------------");
        System.out.println("member : " + member);
        System.out.println("---------------------");

        if (!memberUpdateForm.getPassword1().equals(memberUpdateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordIncorrect",
                    "2개의 패스워드가 일치하지 않습니다.");

            model.addAttribute("member", member);
            return "member/editProfile_form";
        }
        memberService.update(memberUpdateForm, member);
        return "redirect:/member/myPage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete")
    public String delete_account(MemberDeleteForm memberDeleteForm) {
        return "member/deleteAccount_form";
    }

    @PostMapping("/delete")
    public String delete_account(@Valid MemberDeleteForm memberDeleteForm, BindingResult bindingResult, Principal principal) {
        String memberId = principal.getName();
        Member member = memberService.getMemberId(memberId);
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
            return "member/deleteAccount_form";
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
        return "member/delete_account";
    }
}

