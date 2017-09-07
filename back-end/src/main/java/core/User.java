package core;

/**
 * Created by <stanislav.valov@experian.com> on 8/25/2017.
 */
public class User {
    public final String firstName;
    public final String lastName;
    public final String email;
    public final String password;
    public final String confirmPass;

    public User(String firstName, String lastName, String email, String password, String confirmPass) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.confirmPass = confirmPass;
    }
}
