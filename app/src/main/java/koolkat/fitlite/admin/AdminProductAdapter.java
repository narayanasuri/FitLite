package koolkat.fitlite.admin;

import android.support.v7.widget.RecyclerView;
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

    @Override
    public AdminProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.price_card_layout, parent, false);
        AdminProductAdapter.ViewHolder myViewHolder = new AdminProductAdapter.ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(AdminProductAdapter.ViewHolder holder, int position) {

        Oil oil = oils.get(position);
        String title = oil.getTitle();
        String price = "Rs. " + oil.getPrice() + "/" + oil.getQuantity();
        holder.titleTextView.setText(title);
        holder.priceTextView.setText(price);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView priceTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.price_card_title);
            priceTextView = (TextView) itemView.findViewById(R.id.price_card_price);
        }
    }

}
