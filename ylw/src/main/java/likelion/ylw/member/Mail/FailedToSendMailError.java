package likelion.ylw.member.Mail;

public class FailedToSendMailError extends RuntimeException {
    public FailedToSendMailError(String message) {
        super(message);
    }
}
