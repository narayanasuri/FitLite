package koolkat.fitlite;

/**
 * Created by Admin on 4/9/2017.
 */

public class UserInformation {

    public String id;
    public String username;
    public String phonenumber;

    public UserInformation(){

    }

    public UserInformation(String username, String phonenumber) {
        this.username = username;
        this.phonenumber = phonenumber;
    }

    public String getUsername() {
        return username;
    }

    public String getPhonenumber() {
        return phonenumber;
    }
}
