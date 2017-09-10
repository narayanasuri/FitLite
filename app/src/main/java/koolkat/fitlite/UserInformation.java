package koolkat.fitlite;

/**
 * Created by Admin on 4/9/2017.
 */

public class UserInformation {

    private int numberOfOrders;
    private String username;
    private String phonenumber;

    public UserInformation() {

    }

    public UserInformation(int numberOfOrders, String username, String phonenumber) {
        this.numberOfOrders = numberOfOrders;
        this.username = username;
        this.phonenumber = phonenumber;
    }

    public int getNumberOfOrders() {
        return numberOfOrders;
    }

    public String getUsername() {
        return username;
    }

    public String getPhonenumber() {
        return phonenumber;
    }
}
