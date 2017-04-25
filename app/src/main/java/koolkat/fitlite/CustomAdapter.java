package koolkat.fitlite;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<String> oilTypes;
    private List<Integer> oilQuantitities;
    private List<Integer> pricey;
    private List<String> statuses;
    private List<Integer> discounts;
    private List<String> dates;

    public CustomAdapter(List<String> oilTypes, List<Integer> oilQuantitities, List<Integer> pricey, List<String> statuses, List<Integer> discounts, List<String> dates) {
        this.oilTypes = oilTypes;
        this.oilQuantitities = oilQuantitities;
        this.pricey = pricey;
        this.statuses = statuses;
        this.discounts = discounts;
        this.dates = dates;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView oiltypetv;
        TextView oilquantitytv;
        TextView statustv;
        TextView pricetv;
        TextView discounttv;
        TextView datetv;


        public ViewHolder(View itemView) {
            super(itemView);
            oiltypetv = (TextView) itemView.findViewById(R.id.card_oiltype);
            oilquantitytv = (TextView) itemView.findViewById(R.id.card_oilquantity);
            statustv = (TextView) itemView.findViewById(R.id.card_status);
            pricetv = (TextView) itemView.findViewById(R.id.card_price);
            discounttv = (TextView) itemView.findViewById(R.id.card_discount);
            datetv = (TextView) itemView.findViewById(R.id.card_date);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CustomAdapter.ViewHolder viewHolder, int i) {

        viewHolder.oiltypetv.setText("Oil : " + oilTypes.get(i));
        String quant = oilQuantitities.get(i).toString();
        String price = pricey.get(i).toString();
        viewHolder.oilquantitytv.setText("Quantity : " + quant + " litres");
        viewHolder.pricetv.setText("Price : ₹" + price + "/-");
        viewHolder.datetv.setText(dates.get(i));
        viewHolder.statustv.setText("Status : " + statuses.get(i));
        if (discounts.get(i) == 0)
            viewHolder.discounttv.setVisibility(View.GONE);
        else
            viewHolder.discounttv.setText(discounts.get(i) + " litres discounted as ₹15/litre");
    }

    @Override
    public int getItemCount() {
        return oilTypes.size();
    }
}