package main.core;

/**
 * @author Stanislav Valov <Stanislav.Valov@experian.com>
 */
public class Error {
    public final String message;

    /**
     * Creates a new Error by providing path and a message.
     * @param message the error message that causes the violation
     */
    public Error(String message) {
        this.message = message;
    }
}
