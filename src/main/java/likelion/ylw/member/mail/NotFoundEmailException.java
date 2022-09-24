package likelion.ylw.member.mail;

public class NotFoundEmailException extends RuntimeException {
    public NotFoundEmailException(String message) {
        super(message);
    }
}
