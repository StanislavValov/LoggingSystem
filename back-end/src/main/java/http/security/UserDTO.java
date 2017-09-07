package http.security;

/**
 * @author Stanislav Valov <Stanislav.Valov@experian.com>
 */
public class UserDTO {
    private String firstName;
    private String password;
    private String lastName;
    private String email;
    private String confirmPass;

    @SuppressWarnings("unused")
    public UserDTO() {
    }

    public UserDTO(String firstName, String password) {
        this.firstName = firstName;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPassword() {
        return password;
    }
}
