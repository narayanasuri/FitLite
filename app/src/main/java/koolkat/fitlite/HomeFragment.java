package koolkat.fitlite;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Admin on 4/13/2017.
 */

public class HomeFragment extends Fragment {

    private ProgressDialog progressDialog;
    private TextView type1tv;
    private TextView type2tv;
    private TextView type3tv;
    private TextView type4tv;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        type1tv = (TextView) view.findViewById(R.id.oiltypeAtv);
        type2tv = (TextView) view.findViewById(R.id.oiltypeBtv);
        type3tv = (TextView) view.findViewById(R.id.oiltypeCtv);
        type4tv = (TextView) view.findViewById(R.id.oiltypeDtv);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setMessage("Retrieving Prices, Please Wait");
        progressDialog.show();
        progressDialog.setCancelable(false);

        databaseReference.child("products").child("oils").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String typeAprice = dataSnapshot.child("1").child("price").getValue().toString();
                final String typeBprice = dataSnapshot.child("2").child("price").getValue().toString();
                final String typeCprice = dataSnapshot.child("3").child("price").getValue().toString();
                final String typeDprice = dataSnapshot.child("4").child("price").getValue().toString();

                type1tv.setText(typeAprice + "/litre");
                type2tv.setText(typeBprice + "/litre");
                type3tv.setText(typeCprice + "/litre");
                type4tv.setText(typeDprice + "/litre");
                progressDialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }
}
