package main.persistent;

import java.util.Date;

/**
 * @author Stanislav Valov <stanislav.valov@clouway.com>
 */

//Shoul be used instead of User object!
public class UserEntity {
  public final String id;
  public final String firstName;
  public final String lastName;
  public final String email;
  public final Date dateOfBirth;

  public UserEntity(String id, String firstName, String lastName, String email, Date dateOfBirth) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.dateOfBirth = dateOfBirth;
  }

}
