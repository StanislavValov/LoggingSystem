package persistent;

import core.User;

import java.util.Optional;

/**
 * @author Stanislav Valov <Stanislav.Valov@experian.com>
 */
public interface UserRepository {
    Optional<Object> authenticate(String nickName, String password);

    void register(User user);

    boolean userExists(String username);
}
