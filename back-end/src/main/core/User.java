package main.core;

import java.util.Date;

/**
 * Created by Stan on 22.7.2016 г..
 */
public class User {
    public final String nickname;
    public final String firstName;
    public final String lastName;
    public final String email;
    public final Date dateOfBirth;
    public final String imageData;

    public User(String nickname, String firstName, String lastName, String email, Date dateOfBirth, String imageData) {
        this.nickname = nickname;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.imageData = imageData;
    }
}
