package koolkat.fitlite;

/**
 * Created by Admin on 4/13/2017.
 */

class OilRequest {

    private int requestId;
    private String oilType;
    private int oilQuantity;
    private String status;
    private int price;
    private int discount;
    private String orderdate;

    public OilRequest() {

    }

    public OilRequest(int requestId, String oilType, int oilQuantity, String status, int price, int discount, String orderdate) {
        this.requestId = requestId;
        this.oilType = oilType;
        this.oilQuantity = oilQuantity;
        this.status = status;
        this.price = price;
        this.discount = discount;
        this.orderdate = orderdate;
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

    public String getOrderdate(){ return orderdate; }

}