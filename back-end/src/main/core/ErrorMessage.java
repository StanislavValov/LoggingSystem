package main.core;

/**
 * @author Stanislav Valov <Stanislav.Valov@experian.com>
 */
public final class ErrorMessage {
    private  final String message;

    public ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
