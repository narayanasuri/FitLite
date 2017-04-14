package koolkat.fitlite;

/**
 * Created by Admin on 4/13/2017.
 */

public class OilRequest {

    public int requestId;
    public String oilType;
    public int oilQuantity;

    public OilRequest() {
    }

    public OilRequest(int requestId, String oilType, int oilQuantity) {
        this.requestId = requestId;
        this.oilType = oilType;
        this.oilQuantity = oilQuantity;
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
}