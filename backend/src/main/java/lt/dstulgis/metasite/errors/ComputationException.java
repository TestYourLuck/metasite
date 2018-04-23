package lt.dstulgis.metasite.errors;

/**
 * Common error for unexpected behaviour.
 */
public class ComputationException extends RuntimeException {

    public ComputationException(String message) {
        super(message);
    }

    public ComputationException(String message, Throwable e) {
        super(message, e);
    }
}
