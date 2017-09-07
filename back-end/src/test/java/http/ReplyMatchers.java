package http;

import com.google.gson.Gson;
import com.google.sitebricks.headless.Reply;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Field;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.junit.Assert.fail;

/**
 * @author Stanislav Valov <stanislav.valov@clouway.com>
 */
public class ReplyMatchers {
  private static Gson gson = new Gson();

  public static Matcher<Reply<?>> contains(final Object value) {
    return new TypeSafeMatcher<Reply<?>>() {
      @Override
      public boolean matchesSafely(Reply<?> reply) {
        String firstString = gson.toJson(value);
        String secondString = gson.toJson(property("entity", reply));

        return firstString.equals(secondString);
      }

      public void describeTo(Description description) {
        description.appendText("reply value was different from expected one");
      }
    };
  }

  public static Matcher<Reply<?>> isOk() {
    return returnCodeMatcher(SC_OK);
  }

  private static Matcher<Reply<?>> returnCodeMatcher(final int expectedCode) {
    return new TypeSafeMatcher<Reply<?>>() {
      @Override
      public boolean matchesSafely(Reply<?> reply) {
        Integer status = property("status", reply);
        return Integer.valueOf(expectedCode).equals(status);
      }

      public void describeTo(Description description) {
        description.appendText("status of the replay was different from expected");
      }
    };
  }


  private static <T> T property(String fieldName, Reply<?> reply) {
    Field field = null;
    try {
      field = reply.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      T actual = (T) field.get(reply);

      return actual;
    } catch (NoSuchFieldException e) {
      fail(e.getMessage());
    } catch (IllegalAccessException e) {
      fail(e.getMessage());
    } finally {
      if (field != null) {
        field.setAccessible(false);
      }
    }
    return null;
  }
}
