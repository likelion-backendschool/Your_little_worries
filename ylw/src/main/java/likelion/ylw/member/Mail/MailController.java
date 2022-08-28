package likelion.ylw.member.Mail;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping(value = "/mail", method = RequestMethod.GET)
@Controller
public class MailController {

    @GetMapping("/auth/{authcode}")
    public String send(@PathVariable("authcode") String authcode) throws IOException {

         return "redirect:/new_password";
    }

    @PostMapping("/send")
    public String send_newPW() throws IOException {
        Example.sendEmail();
        return "mail_send";
    }
}
