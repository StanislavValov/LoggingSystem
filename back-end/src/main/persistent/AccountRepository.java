package main.persistent;

import main.core.User;

import java.util.List;

/**
 * @author Stanislav Valov <stanislav.valov@clouway.com>
 */
public interface AccountRepository {

  void create(User user) throws DuplicationException;

  void update(User user);

  void delete(String id);

  List<User> findAll();

  void importImage(String nickname, String imageData);
}
