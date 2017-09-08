package koolkat.fitlite;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 4/15/2017.
 */

public class AdminOrderCustomAdapter extends RecyclerView.Adapter<AdminOrderCustomAdapter.ViewHolder> {

    private List<String> usernames = new ArrayList<>();
    private List<String> phonenumbers = new ArrayList<>();
    private List<Integer> orderNumbers = new ArrayList<>();
    private DatabaseReference databaseReference;

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
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(final AdminOrderCustomAdapter.ViewHolder holder, final int position) {

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
