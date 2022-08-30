package likelion.ylw.member.Mail;

import likelion.ylw.member.Member;
import likelion.ylw.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RequestMapping(value = "/mail", method = RequestMethod.GET)
@Controller
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;
    private final MemberService memberService;
    private String email;

    @GetMapping("/auth/2377655")
    public String send() throws IOException {

         return "new_password";
    }

    @PostMapping("/send")
    public String send_newPW(Model model, @Valid MailForm mailForm, BindingResult bindingResult) throws IOException {
        String email = mailForm.getEmail();
        Example.sendEmail(email);
        this.email = email;
        return "mail_send";
    }

    @GetMapping("/reset")
    public String send_form() {

        return "sendEmail_form";
    }

    @PostMapping("/password/new")
    public String password_new(@Valid PwForm pwForm, MailForm mailForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "new_password";
        }

        if (!pwForm.getPassword1().equals(pwForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordIncorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "new_password";
        }

        Member member = memberService.findByEmail(this.email);
        mailService.reset(member, pwForm.getPassword1());
        return "redirect:/member/login";
    }
}
