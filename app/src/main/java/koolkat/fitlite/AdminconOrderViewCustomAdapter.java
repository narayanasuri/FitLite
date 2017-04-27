package koolkat.fitlite;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 4/16/2017.
 */

public class AdminconOrderViewCustomAdapter extends RecyclerView.Adapter<AdminconOrderViewCustomAdapter.ViewHolder> {

    private List<String> oilTypes = new ArrayList<>();
    private List<Integer> quantities = new ArrayList<>();
    private List<Integer> prices = new ArrayList<>();
    private List<String> status = new ArrayList<>();
    private List<String> dates = new ArrayList<>();

    public AdminconOrderViewCustomAdapter(List<String> oilTypes, List<Integer> quantities, List<Integer> prices, List<String> status, List<String> dates) {
        this.oilTypes = oilTypes;
        this.quantities = quantities;
        this.prices = prices;
        this.status = status;
        this.dates = dates;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView oiltypetv;
        final TextView quantitytv;
        final TextView pricetv;
        final TextView statustv;
        final TextView datetv;


        public ViewHolder(View itemView) {
            super(itemView);
            oiltypetv = (TextView) itemView.findViewById(R.id.ordercard_oil);
            quantitytv = (TextView) itemView.findViewById(R.id.ordercard_quantity);
            pricetv = (TextView) itemView.findViewById(R.id.ordercard_price);
            statustv = (TextView) itemView.findViewById(R.id.ordercard_status);
            datetv = (TextView) itemView.findViewById(R.id.ordercard_date);
        }
    }

    @Override
    public AdminconOrderViewCustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_order_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdminconOrderViewCustomAdapter.ViewHolder holder, int position) {

        if (status.get(position).matches("Approved")) {
            String oiltype = "Oil : " + oilTypes.get(position);
            String quantity = "Quantity : " + quantities.get(position).toString();
            String price = "Price : ₹" + prices.get(position).toString();
            String stat = "Status : " + status.get(position);
            holder.oiltypetv.setText(oiltype);
            holder.quantitytv.setText(quantity);
            holder.pricetv.setText(price);
            holder.statustv.setText(stat);
            holder.datetv.setText(dates.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return oilTypes.size();
    }

}
