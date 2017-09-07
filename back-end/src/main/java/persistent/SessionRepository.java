package persistent;

/**
 * @author Stanislav Valov <Stanislav.Valov@experian.com>
 */
public interface SessionRepository {
    void addUser(String nickname, Object sessionId);
}
