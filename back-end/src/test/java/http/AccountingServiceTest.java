package http;

import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import core.Account;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import persistent.AccountRepository;

import java.util.Date;

import static org.junit.Assert.assertThat;

/**
 * @author Stanislav Valov <stanislav.valov@clouway.com>
 */
@Ignore
public class AccountingServiceTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private AccountRepository repository = context.mock(AccountRepository.class);
  private AccountService service = new AccountService(repository);

  @Test
  public void create() {
    AccountDTO dto = new AccountDTO("Torbalan", "bla", "mail@","blabal", new Date());
    final Account account = new Account("Torbalan", "bla", "mail@","email", new Date());

    Request request = RequestFactory.requestWith(dto);

    context.checking(new Expectations(){{
      oneOf(repository).create(account);
    }});

    Reply<?> reply = service.create(request);

    assertThat(reply, ReplyMatchers.isOk());
  }

  @Test
  public void find() {
    final Account account = new Account("Torbalan", "bla", "mail@", "aaaaaaaaa", new Date());

    context.checking(new Expectations(){{
      oneOf(repository).findAll();
      will(returnValue(newArrayList(account)));
    }});

    Reply<?> reply = service.findAll();

    assertThat(reply, ReplyMatchers.contains(newArrayList(account)));
    assertThat(reply, ReplyMatchers.isOk());
  }

  @Test
  public void delete() {
    context.checking(new Expectations(){{
      oneOf(repository).delete("id");
    }});

    Reply<?> reply = service.delete("id");

    assertThat(reply, ReplyMatchers.isOk());
  }
}
