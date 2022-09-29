package likelion.ylw.member.mail;

import likelion.ylw.member.Member;
import likelion.ylw.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RequestMapping(value = "/mail", method = RequestMethod.GET)
@Controller
@RequiredArgsConstructor
@Component
public class MailController {

    @Value("${spring.sendgrid.api-key}")
    private String sendGridKey;

    @Value("${spring.sendgrid.url}")
    private String emailUrl;

    @Value("${spring.sendgrid.email}")
    private String adminEmail;

    private final MailService mailService;
    private final MemberService memberService;
    private String email;
    private boolean isExist = false;

    @GetMapping("/auth/2377655")
    public String send(PwForm pwForm) throws IOException {
        return "member/member_new_pw_form";
    }

    @GetMapping("/send")
    public String send_form(MailForm mailForm) {
        return "member/mail/mail_send_form";
    }

    @PostMapping("/reset")
    public String send_newPW(Model model, @Valid MailForm mailForm, BindingResult bindingResult) throws IOException {
        if(bindingResult.hasErrors()) {
            return "member/mail/mail_send_form";
        }
        String email = mailForm.getEmail();
        try {
            Member member = memberService.findByEmail(email);
            if (member == null){
                bindingResult.rejectValue("email", "NotFoundMemberMatchedEmail",
                        "해당 이메일로 가입된 회원이 없습니다");
                return "member/mail/mail_send_form";
            }
            Example.sendEmail(email, adminEmail, sendGridKey, emailUrl);

            this.email = email;
            return "member/mail/mail_complete";
        } catch(NotFoundEmailException e) {
            bindingResult.reject("notFoundEmail", e.getMessage());
            System.out.println(e.getMessage());
            return "member/mail/mail_send_form";
        }
    }

    @PostMapping("/reset/password") //new/password
    public String password_new(@Valid PwForm pwForm, MailForm mailForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/member_new_pw_form";
        }

        if (!pwForm.getPassword1().equals(pwForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordIncorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "member/member_new_pw_form";
        }

        Member member = memberService.findByEmail(this.email);
        mailService.reset(member, pwForm.getPassword1());
        return "redirect:/member/login";
    }
}
