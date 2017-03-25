package main.exceptions;

/**
 * @author Stanislav Valov <Stanislav.Valov@experian.com>
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
