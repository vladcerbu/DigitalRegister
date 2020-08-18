package Exceptions;

/**
 * InvalidDateException - To throw when a date is not valid
 */
public class InvalidDateException extends RuntimeException {
    private final String err;
    public InvalidDateException(String err) { this.err = err; }
    public String getErr() { return err; }
}
