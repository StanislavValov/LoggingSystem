package main.transport;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author Stanislav Valov <Stanislav.Valov@experian.com>
 */
public class ValidationException extends RuntimeException {
    /**
     * Adds a new Error
     *
     * @param message the error message
     * @return the newly registered builder.
     */
    public static Builder newError(String message) {
        return new Builder(message);
    }

    /**
     * A builder class for building of errors.
     */
    public static class Builder {
        private List<Error> errors = Lists.newArrayList();

        public Builder(String message) {
            errors.add(new Error(message));
        }

        public void fire() {
            throw new ValidationException(errors);
        }
    }

    public final List<Error> errors;

    public ValidationException(List<Error> errors) {
        this.errors = errors;
    }

}
