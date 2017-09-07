package http.security;

import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Post;
import core.User;
import persistent.UserRepository;
import transport.Json;

/**
 * @author Stanislav Valov <Stanislav.Valov@experian.com>
 */
@Service
@At("/api/v1/register")
public class RegisterService {
    private UserRepository userRepository;

    @Inject
    public RegisterService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Post
    public Reply<?> register(Request request) {
        UserDTO dto = request.read(UserDTO.class).as(Json.class);

        User user = adapt(dto);

        if (userRepository.userExists(user.firstName)){
            return Reply.with("User with name " + user.firstName + " already exists").badRequest();
        }

        userRepository.register(user);

        return Reply.with("Account was created").ok();
    }

    private User adapt(UserDTO dto) {
        return new User(dto.getFirstName(), "", "", dto.getPassword(), "");
    }


}
