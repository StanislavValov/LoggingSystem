package main.transport;

/**
 * @author Stanislav Valov <Stanislav.Valov@experian.com>
 */
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.sitebricks.client.Transport;
import main.core.Error;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

public class Json implements Transport {
    private ObjectValidator objectValidator;
    private Gson gson;

    @Inject
    public Json(Gson gson, ObjectValidator objectValidator) {
        this.gson = gson;
        this.objectValidator = objectValidator;
    }

    public <T> T in(InputStream inputStream, Class<T> tClass) throws IOException {
        T entity = gson.fromJson(new InputStreamReader(inputStream, "UTF-8"), tClass);
        check(entity);
        return entity;
    }

    @Override
    public <T> T in(InputStream inputStream, TypeLiteral<T> typeLiteral) throws IOException {
        T entity = gson.fromJson(new InputStreamReader(inputStream,"UTF-8"), typeLiteral.getType());
        check(entity);
        return entity;
    }

    public <T> void out(OutputStream outputStream, Class<T> tClass, T t) {
        String json = gson.toJson(t);
        try {
            outputStream.write(json.getBytes("UTF8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String contentType() {
        return "application/json";
    }

    private <T> void check(T entity) {
        List<Error> errors = objectValidator.validate(entity);
        boolean hasErrors = errors.size() > 0;
        if (hasErrors) {
            throw new ValidationException(errors);
        }
    }
}

