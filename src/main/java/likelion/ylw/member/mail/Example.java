package likelion.ylw.member.mail;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
@Slf4j
public abstract class Example {

    public static void sendEmail(String email, String adminEmail, String sendGridKey, String emailUrl) throws IOException {
        Email from = new Email(adminEmail);
        Email to = new Email(email);

        String subject = "이메일 인증";
        String text = """
                <div><h1>비밀번호 재설정</h1></div>
                <div><h5>아래의 링크를 클릭하여 비밀번호를 새로 설정해 주세요.</h5></div>
                <div><h2><a href="%s/%s">새로운 비밀번호 설정하기</a></h2></div>
                """.formatted(emailUrl, 2377655);
        Content content = new Content("text/html", text);

        Mail mail = new Mail(from, subject, to, content);

        SendGrid sendGrid = new SendGrid(sendGridKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            log.info("{}", response.getStatusCode());
            log.info("{}", response.getHeaders());
            log.info("{}", response.getBody());
        } catch (IOException ex) {
            throw new FailedToSendMailError("can't send message");
        }
    }
}

