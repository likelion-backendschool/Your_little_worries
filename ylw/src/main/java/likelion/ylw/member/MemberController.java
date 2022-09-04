package likelion.ylw.member;

import likelion.ylw.member.Mail.MailForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/signup")
    public String signup(MemberCreateForm memberCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordIncorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        try {
            memberService.create(memberCreateForm.getMemberId(),
                    memberCreateForm.getPassword1(), memberCreateForm.getEmail(), memberCreateForm.getNickname());
        } catch (SignupEmailDuplicatedException e) {
            bindingResult.reject("signupEmailDuplicated", e.getMessage());
            return "signup_form";
        } catch (SignupMemberIdDuplicatedException e) {
            bindingResult.reject("signupUsernameDuplicated", e.getMessage());
            return "signup_form";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    @GetMapping("/findID")
    public String find_id() {

        return "findID_form";
    }

    @PostMapping("/findID")
    public String find_id_post(Model model, @Valid MailForm mailForm, BindingResult bindingResult) {
        Member member = memberService.findByEmail(mailForm.getEmail());
        model.addAttribute("member", member);

        return "findID_result";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage")
    public String my_page(Principal principal, Model model) {
        System.out.println("principal getName(): " + principal.getName());
        Member member = memberService.getMemberId(principal.getName());
        model.addAttribute("member", member);
        return "myPage_form";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/edit")
    public String my_page_edit(Principal principal, Model model) {
        Member member = memberService.getMemberId(principal.getName());
        model.addAttribute("member", member);
        return "editProfile_form";
    }

    @PutMapping("/profile/edit")
    public String my_page_edit() {
        return "redirect:/";
    }

    @GetMapping("/delete") //delete_form 추가하기
    public String delete() {
        return "delete_form";
    }
}
