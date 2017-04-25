package koolkat.fitlite;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 4/15/2017.
 */

public class AdminOrderCustomAdapter extends RecyclerView.Adapter<AdminOrderCustomAdapter.ViewHolder> {

    private List<String> usernames = new ArrayList<>();
    private List<String> phonenumbers = new ArrayList<>();
    private List<Integer> orderNumbers = new ArrayList<>();

    public AdminOrderCustomAdapter(List<String> usernames, List<String> phonenumbers, List<Integer> orderNumbers) {
        this.usernames = usernames;
        this.phonenumbers = phonenumbers;
        this.orderNumbers = orderNumbers;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernametv;
        TextView phonenumbertv;
        TextView ordernotv;


        public ViewHolder(View itemView) {
            super(itemView);
            usernametv = (TextView) itemView.findViewById(R.id.card_username);
            phonenumbertv = (TextView) itemView.findViewById(R.id.card_phno);
            ordernotv = (TextView) itemView.findViewById(R.id.card_orderno);
        }
    }


    @Override
    public AdminOrderCustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card_layout, parent, false);
        AdminOrderCustomAdapter.ViewHolder myViewHolder = new AdminOrderCustomAdapter.ViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(AdminOrderCustomAdapter.ViewHolder holder, int position) {

        String usr = "Username : " + usernames.get(position);
        String phn = "Phone Number : " + phonenumbers.get(position);
        String ord = "Total number of orders : " + orderNumbers.get(position).toString();
        holder.usernametv.setText(usr);
        holder.phonenumbertv.setText(phn);
        holder.ordernotv.setText(ord);

    }

    @Override
    public int getItemCount() {
        return usernames.size();
    }

}
