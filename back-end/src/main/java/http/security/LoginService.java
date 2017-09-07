package http.security;

import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Post;
import core.User;
import org.bson.codecs.IdGenerator;
import persistent.SessionRepository;
import persistent.UserRepository;
import transport.Json;

/**
 * @author Stanislav Valov <Stanislav.Valov@experian.com>
 */
@Service
@At("/api/v1/login")
public class LoginService {
    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    private IdGenerator idGenerator;

    @Inject
    public LoginService(UserRepository userRepository, SessionRepository sessionRepository, IdGenerator idGenerator) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.idGenerator = idGenerator;
    }

    @Post
    @At("/authenticate")
    public Reply<?> authenticate(Request request) {
        UserDTO dto = request.read(UserDTO.class).as(Json.class);

        User user = adapt(dto);

        String firstName = user.firstName;
        String password = user.password;

        if (!userRepository.authenticate(firstName, password).isPresent()) {
            return Reply.with("Authentication failed").badRequest();
        }

        Object sessionId = idGenerator.generate();
        sessionRepository.addUser(firstName, sessionId);
//        response.addCookie(new Cookie("SID", (String) sessionId));

        return Reply.saying().ok();
    }

    private User adapt(UserDTO dto) {
        return new User(dto.getFirstName(), "", "", dto.getPassword(), "");
    }


}
