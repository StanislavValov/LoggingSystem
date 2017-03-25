package main.http;

import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;


import main.persistent.AccountRepository;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

import static com.google.common.collect.Lists.newArrayList;
import static main.http.ReplyMatchers.contains;
import static main.http.ReplyMatchers.isOk;
import static main.http.RequestFactory.requestWith;
import static org.junit.Assert.assertThat;

/**
 * @author Stanislav Valov <stanislav.valov@clouway.com>
 */
public class AccountingServiceTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private AccountRepository repository = context.mock(AccountRepository.class);
  private AccountService service = new AccountService(repository);

  @Test
  public void create() {
    UserDTO dto = new UserDTO("Torbalan", "bla", "mail@","blabal", new Date());
    final User user = new User("Torbalan", "bla", "mail@","email", new Date());

    Request request = requestWith(dto);

    context.checking(new Expectations(){{
      oneOf(repository).create(user);
    }});

    Reply<?> reply = service.create(request);

    assertThat(reply, isOk());
  }

  @Test
  public void find() {
    final User user = new User("Torbalan", "bla", "mail@", "aaaaaaaaa", new Date());

    context.checking(new Expectations(){{
      oneOf(repository).findAll();
      will(returnValue(newArrayList(user)));
    }});

    Reply<?> reply = service.findAll();

    assertThat(reply, contains(newArrayList(user)));
    assertThat(reply, isOk());
  }

  @Test
  public void delete() {
    context.checking(new Expectations(){{
      oneOf(repository).delete("id");
    }});

    Reply<?> reply = service.delete("id");

    assertThat(reply, isOk());
  }
}
