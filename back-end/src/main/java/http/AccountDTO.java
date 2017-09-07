package http;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @author Stanislav Valov <stanislav.valov@clouway.com>
 */

public class AccountDTO {

  @NotNull(message = "Nickname is required.")
  @NotEmpty(message = "Nickname cannot be empty.")
  @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Please provide only letters.")
  public String nickname;

  public String firstName;
  public String lastName;
  public String email;

  @Pattern(regexp = "^(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$", message = "Please provide correct date of birth.")
  public String dateOfBirth;
  public String imageData;

  public AccountDTO(String nickname, String firstName, String lastName, String email, String dateOfBirth, String imageData) {
    this.nickname = nickname;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.dateOfBirth = dateOfBirth;
    this.imageData = imageData;
  }

  @SuppressWarnings("unused")
  public AccountDTO() {
  }
}
