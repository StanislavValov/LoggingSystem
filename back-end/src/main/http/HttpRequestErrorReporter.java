package main.http;

import com.google.sitebricks.headless.Reply;
import main.core.Error;
import main.core.ErrorMessage;
import main.core.Errors;
import main.exceptions.NotFoundException;
import main.exceptions.ProgrammerMistake;
import main.transport.Json;
import main.transport.ValidationException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * @author Stanislav Valov <Stanislav.Valov@experian.com>
 */
public class HttpRequestErrorReporter implements MethodInterceptor {
    private final Logger logger = LoggerFactory.getLogger(HttpRequestErrorReporter.class);

    private final String defaultInternalServerError;

    /**
     * Creates a new HttpRequestErrorReporter by providing the error message to be used when internal server error is occurred.
     *
     */
    public HttpRequestErrorReporter(String defaultInternalServerError) {
        this.defaultInternalServerError = defaultInternalServerError;
    }

    /**
     * Intercepts the provided MethodInvocation by promoting exception handling in case such ones exist.
     * <p/>
     */
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object result;

        try {

            result = methodInvocation.proceed();

        } catch (ValidationException e) {

            List<ErrorMessage> messages = newArrayList();

            for (Error error : e.errors) {
                messages.add(new ErrorMessage(error.message));
            }

            return Reply.with(new Errors(messages).getErrorMessages()).status(SC_BAD_REQUEST).as(Json.class);

        } catch (ClassCastException e) {

            return Reply.with(new Errors(newArrayList(new ErrorMessage(defaultInternalServerError))).getErrorMessages()).status(SC_BAD_REQUEST).as(Json.class);

        } catch (NotFoundException e) {

            return Reply.with(new Errors(newArrayList(new ErrorMessage(e.getMessage()))).getErrorMessages()).status(SC_NOT_FOUND).as(Json.class);

        } catch (RuntimeException e) {

            logger.error("Exception stack trace", e);

            if(isNullOrEmpty(e.getMessage())) {
                return Reply.with(new Errors(newArrayList(new ErrorMessage(defaultInternalServerError))).getErrorMessages()).error().as(Json.class);
            }

            return Reply.with(new Errors(newArrayList(new ErrorMessage(e.getMessage()))).getErrorMessages()).error().as(Json.class);

        }

        if (!(result instanceof Reply)) {
            throw new ProgrammerMistake("Wrong interceptor binding was performed. It seems that methods that are not returning" +
                " Reply objects are matched.");
        }

        return result;
    }
}
