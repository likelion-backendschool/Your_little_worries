package likelion.ylw.member.Mail;

public class NotFoundEmailException extends RuntimeException {
    public NotFoundEmailException(String message) {
        super(message);
    }
}
