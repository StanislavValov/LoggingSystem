package main.http;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @author Stanislav Valov <stanislav.valov@clouway.com>
 */
public class UserDTO {

  @NotNull(message = "Nickname is required.")
  @NotEmpty(message = "Nickname cannot be empty.")
  @Pattern(regexp = "\\p{L}", message = "Please provide only letters.")
  public String nickname;

  public String firstName;
  public String lastName;
  public String email;
  public Date dateOfBirth;
  public String imageData;

  public UserDTO(String nickname, String firstName, String lastName, String email, Date dateOfBirth, String imageData) {
    this.nickname = nickname;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.dateOfBirth = dateOfBirth;
    this.imageData = imageData;
  }

  @SuppressWarnings("unused")
  public UserDTO() {
  }
}
