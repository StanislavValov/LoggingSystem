package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.sitebricks.SitebricksModule;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import http.security.LoginService;
import http.security.RegisterService;
import org.bson.codecs.IdGenerator;
import org.bson.codecs.ObjectIdGenerator;
import transport.HibernateObjectValidator;
import transport.ObjectValidator;
import org.hibernate.validator.HibernateValidator;

import javax.validation.Validation;
import javax.validation.Validator;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.only;
import static com.google.inject.matcher.Matchers.returns;

/**
 * @author Stanislav Valov <stanislav.valov@clouway.com>
 */
class HttpModule extends AbstractModule{

  @Override
  protected void configure() {
    bind(ObjectValidator.class).to(HibernateObjectValidator.class).in(Singleton.class);
    bind(IdGenerator.class).to(ObjectIdGenerator.class).in(Singleton.class);

    final Module pageBricks = new SitebricksModule() {
      @Override
      protected void configureSitebricks() {
        at("/api/v1/accounts").serve(AccountService.class);
        at("/api/v1/login").serve(LoginService.class);
        at("/api/v1/register").serve(RegisterService.class);
      }
    };

    install(pageBricks);

    bindInterceptor(annotatedWith(Service.class), returns(only(Reply.class)), new HttpRequestErrorReporter("Internal Server Error"));
  }

  @Provides
  @Singleton
  public Gson getGson() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.serializeNulls();
    return gsonBuilder.create();
  }

  @Provides
  @Singleton
  public Validator getValidator() {
    return Validation.byProvider(HibernateValidator.class).configure().buildValidatorFactory().getValidator();
  }


}
