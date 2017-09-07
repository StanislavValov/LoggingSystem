package persistent;

import core.Account;

import java.util.List;

/**
 * @author Stanislav Valov <stanislav.valov@clouway.com>
 */
public interface AccountRepository {

  void create(Account account) throws DuplicationException;

  void update(Account account);

  void delete(String id);

  List<Account> findAll();

  void importImage(String nickname, String imageData);
}
