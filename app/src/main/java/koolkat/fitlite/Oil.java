package koolkat.fitlite;

/**
 * Created by Admin on 9/8/2017.
 */

public class Oil {

    private String title;
    private int price;
    private float quantity;

    public Oil() {

    }

    public Oil(String title, int price, float quantity) {
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }
}
