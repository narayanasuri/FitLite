package koolkat.fitlite.admin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import koolkat.fitlite.R;

/**
 * Created by Admin on 4/15/2017.
 */

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.ViewHolder> {

    private List<String> usernames = new ArrayList<>();
    private List<String> phonenumbers = new ArrayList<>();
    private List<Integer> orderNumbers = new ArrayList<>();

    public AdminUserAdapter(List<String> usernames, List<String> phonenumbers, List<Integer> orderNumbers) {
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
    public AdminUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card_layout, parent, false);
        AdminUserAdapter.ViewHolder myViewHolder = new AdminUserAdapter.ViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(final AdminUserAdapter.ViewHolder holder, final int position) {

        String usr = "Username : " + usernames.get(position);
        String phn = "Phone Number : " + phonenumbers.get(position);
        String ordrs = "Number of Requests : " + orderNumbers.get(position);
        holder.usernametv.setText(usr);
        holder.phonenumbertv.setText(phn);
        holder.ordernotv.setText(ordrs);

    }

    @Override
    public int getItemCount() {
        return usernames.size();
    }

}
