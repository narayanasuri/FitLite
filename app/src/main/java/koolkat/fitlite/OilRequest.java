package koolkat.fitlite;

/**
 * Created by Admin on 4/13/2017.
 */

public class OilRequest {

    public int requestId;
    public String oilType;
    public int oilQuantity;
    public String status;
    public int price;
    public int discount;

    public OilRequest() {

    }

    public OilRequest(int requestId, String oilType, int oilQuantity, String status, int price, int discount) {
        this.requestId = requestId;
        this.oilType = oilType;
        this.oilQuantity = oilQuantity;
        this.status = status;
        this.price = price;
        this.discount = discount;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getOilType() {
        return oilType;
    }

    public int getOilQuantity() {
        return oilQuantity;
    }

    public int getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public int getDiscount() {
        return discount;
    }

}