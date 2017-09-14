package koolkat.fitlite;

/**
 * Created by Admin on 4/9/2017.
 */

public class UserInformation {

    private int numberOfOrders;
    private String name;
    private String phonenumber;
    private String refferalcode;
    private String refferedfrom;
    private int literbought;
    private int literreferred;
    private String uniqueid;
    private int first;


    public UserInformation() {

    }

    public UserInformation(int numberOfOrders, String name, String phonenumber, String refferalcode, String refferedfrom, int literbought, int literreferred, String uniqueid, int first) {
        this.numberOfOrders = numberOfOrders;
        this.name = name;
        this.phonenumber = phonenumber;
        this.refferalcode = refferalcode;
        this.refferedfrom = refferedfrom;
        this.literbought = literbought;
        this.literreferred = literreferred;
        this.uniqueid = uniqueid;
        this.first = first;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getNumberOfOrders() {
        return numberOfOrders;
    }

    public void setNumberOfOrders(int numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getRefferalcode() {
        return refferalcode;
    }

    public void setRefferalcode(String refferalcode) {
        this.refferalcode = refferalcode;
    }

    public String getRefferedfrom() {
        return refferedfrom;
    }

    public void setRefferedfrom(String refferedfrom) {
        this.refferedfrom = refferedfrom;
    }

    public int getLiterbought() {
        return literbought;
    }

    public void setLiterbought(int literbought) {
        this.literbought = literbought;
    }

    public int getLiterreferred() {
        return literreferred;
    }

    public void setLiterreferred(int literreferred) {
        this.literreferred = literreferred;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }
}
