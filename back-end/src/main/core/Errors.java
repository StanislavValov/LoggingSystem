package main.core;

import java.util.List;

/**
 * @author Stanislav Valov <Stanislav.Valov@experian.com>
 */
public final class Errors {
    private final List<ErrorMessage> errorMessages;

    public Errors(List<ErrorMessage> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public String getErrorMessages() {
        StringBuilder messageBuilder = new StringBuilder();

        for (ErrorMessage message : errorMessages){
            messageBuilder.append(message.getMessage());
            messageBuilder.append(" ");
        }

        return messageBuilder.toString();
    }

}
