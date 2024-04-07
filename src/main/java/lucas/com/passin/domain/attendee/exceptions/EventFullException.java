package lucas.com.passin.domain.attendee.exceptions;

public class EventFullException extends RuntimeException {

    public EventFullException(String message) {
        super(message);
    }
}
