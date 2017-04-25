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
 * Created by Suri & Kushagra on 4/15/2017.
 */

public class AdminconOrderCustomAdapter extends RecyclerView.Adapter<AdminconOrderCustomAdapter.ViewHolder> {

    private List<String> usernames = new ArrayList<>();
    private List<String> phonenumbers = new ArrayList<>();
    private DatabaseReference databaseReference;

    public AdminconOrderCustomAdapter(List<String> usernames, List<String> phonenumbers) {
        this.usernames = usernames;
        this.phonenumbers = phonenumbers;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView usernametv;
        final TextView phonenumbertv;
        final TextView ordernotv;

        public ViewHolder(View itemView) {
            super(itemView);
            usernametv = (TextView) itemView.findViewById(R.id.card_username);
            phonenumbertv = (TextView) itemView.findViewById(R.id.card_phno);
            ordernotv = (TextView) itemView.findViewById(R.id.card_orderno);
        }
    }


    @Override
    public AdminconOrderCustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card_layout, parent, false);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final AdminconOrderCustomAdapter.ViewHolder holder,final int position) {

        String usr = "Username : " + usernames.get(position);
        String phn = "Phone Number : " + phonenumbers.get(position);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String id=dataSnapshot.child("userids").child(usernames.get(position)).getValue().toString();
                final int confirmed = Integer.parseInt(dataSnapshot.child("calc").child(id).child("confirmedOrders").getValue().toString());
                String ord = "Confirmed orders : " + confirmed;
                holder.ordernotv.setText(ord);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.usernametv.setText(usr);
        holder.phonenumbertv.setText(phn);


    }

    @Override
    public int getItemCount() {
        return usernames.size();
    }

}
