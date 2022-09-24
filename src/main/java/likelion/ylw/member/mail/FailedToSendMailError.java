package likelion.ylw.member.mail;

public class FailedToSendMailError extends RuntimeException {
    public FailedToSendMailError(String message) {
        super(message);
    }
}
