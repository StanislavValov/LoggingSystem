package main.persistent;

import com.google.inject.util.Providers;
import com.mongodb.client.MongoDatabase;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Stanislav Valov <stanislav.valov@clouway.com>
 */
public class PersistentMongoAccountRepositoryTest {

  @Rule
  JUnitRuleMockery context = new JUnitRuleMockery();

  private MongoDatabase database = context.mock(MongoDatabase.class);
  private PersistentMongoAccountRepository repository = new PersistentMongoAccountRepository(Providers.of(database));

  @Test
  public void create() {
    User user = new User("nick", "first", "last", "mail", new Date());

    repository.create(user);
    List<User> users = repository.findAll();

    assertThat(users.size(), is(1));
  }

  @Test
  public void delete() {
    User user = new User("nick", "first", "last", "mail", new Date());

    repository.create(user);
    repository.delete("nick");

    List<User> users = repository.findAll();

    assertThat(users.size(), is(0));
  }
}
