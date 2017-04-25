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

public class AdminOrderViewCustomAdapter extends RecyclerView.Adapter<AdminOrderViewCustomAdapter.ViewHolder> {

    private List<String> oilTypes = new ArrayList<>();
    private List<Integer> quantities = new ArrayList<>();
    private List<Integer> prices = new ArrayList<>();
    private List<String> statuses = new ArrayList<>();
    private List<String> dates = new ArrayList<>();

    public AdminOrderViewCustomAdapter(List<String> oilTypes, List<Integer> quantities, List<Integer> prices, List<String> statuses, List<String> dates) {
        this.oilTypes = oilTypes;
        this.quantities = quantities;
        this.prices = prices;
        this.statuses = statuses;
        this.dates = dates;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView oiltypetv;
        TextView quantitytv;
        TextView pricetv;
        TextView statustv;
        TextView datetv;

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
    public AdminOrderViewCustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_order_card_layout, parent, false);
        AdminOrderViewCustomAdapter.ViewHolder myViewHolder = new AdminOrderViewCustomAdapter.ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(AdminOrderViewCustomAdapter.ViewHolder holder, int position) {

        String oiltype = "Oil : "+oilTypes.get(position);
        String quantity = "Quantity : "+quantities.get(position).toString()+" litres";
        String price = "Price : ₹"+prices.get(position).toString();
        String status = "Status : "+statuses.get(position).toString();
        holder.oiltypetv.setText(oiltype);
        holder.quantitytv.setText(quantity);
        holder.pricetv.setText(price);
        holder.statustv.setText(status);
        holder.datetv.setText(dates.get(position));
    }

    @Override
    public int getItemCount() {
        return oilTypes.size();
    }

}
