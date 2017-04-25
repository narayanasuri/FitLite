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

public class AdminconOrderCustomAdapter extends RecyclerView.Adapter<AdminconOrderCustomAdapter.ViewHolder> {

    private List<String> usernames = new ArrayList<>();
    private List<String> phonenumbers = new ArrayList<>();

    public AdminconOrderCustomAdapter(List<String> usernames, List<String> phonenumbers, List<Integer> orderNumbers) {
        this.usernames = usernames;
        this.phonenumbers = phonenumbers;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernametv;
        TextView phonenumbertv;


        public ViewHolder(View itemView) {
            super(itemView);
            usernametv = (TextView) itemView.findViewById(R.id.card_username);
            phonenumbertv = (TextView) itemView.findViewById(R.id.card_phno);

        }
    }


    @Override
    public AdminconOrderCustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirmed_card_layout, parent, false);
        AdminconOrderCustomAdapter.ViewHolder myViewHolder = new AdminconOrderCustomAdapter.ViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(AdminconOrderCustomAdapter.ViewHolder holder, int position) {

        String usr = "Username : " + usernames.get(position);
        String phn = "Phone Number : " + phonenumbers.get(position);
        holder.usernametv.setText(usr);
        holder.phonenumbertv.setText(phn);


    }

    @Override
    public int getItemCount() {
        return usernames.size();
    }

}
