package Exceptions;

/**
 * ValidationException - To throw when an entity is not valid
 */
public class ValidationException extends RuntimeException {
    private final String err;
    public ValidationException(String err) { this.err = err; }
    public String getErr() { return err; }
}
