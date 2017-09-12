package koolkat.fitlite.admin;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import koolkat.fitlite.Oil;
import koolkat.fitlite.R;

/**
 * Created by Admin on 9/8/2017.
 */

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ViewHolder> {

    private List<Oil> oils = new ArrayList<Oil>();
//    private List<String> oilTitles = new ArrayList<String>();
//    private List<String> oilQuantities = new ArrayList<String>();
//    private List<String> oilPrices = new ArrayList<String>();

    public AdminProductAdapter(List<Oil> oils){
        this.oils = oils;
    }

//    public AdminProductAdapter(List<String> oilTitles, List<String> oilQuantities, List<String> oilPrices){
//
//        this.oilTitles = oilTitles;
//        this.oilQuantities = oilQuantities;
//        this.oilPrices = oilPrices;
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView priceTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.price_card_title);
            priceTextView = (TextView) itemView.findViewById(R.id.price_card_price);
        }

    }

    @Override
    public AdminProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.price_card_layout, parent, false);
        AdminProductAdapter.ViewHolder myViewHolder = new AdminProductAdapter.ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(AdminProductAdapter.ViewHolder holder, int position) {

        Oil oil = oils.get(position);

//        String title = oilTitles.get(position);

        String title = oil.getTitle();
        String price = oil.getPrice()+"";
        String quantity = oil.getQuantity()+"";

//        String priceText = "Rs. " + oilPrices.get(position) + "/" + oilQuantities.get(position);

        String priceText = "Rs. " + price +"/" + quantity + "litres";
        holder.titleTextView.setText(title);
        holder.priceTextView.setText(priceText);

    }

    @Override
    public int getItemCount() {
        return oils.size();
    }

}