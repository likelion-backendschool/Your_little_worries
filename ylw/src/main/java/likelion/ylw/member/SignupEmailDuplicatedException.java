package likelion.ylw.member;

public class SignupEmailDuplicatedException extends RuntimeException{
    public SignupEmailDuplicatedException(String message) {
        super(message);
    }
}
