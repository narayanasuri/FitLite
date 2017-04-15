package koolkat.fitlite;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Admin on 4/13/2017.
 */

public class HomeFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    TextView type1tv, type2tv, type3tv, type4tv;

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

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setMessage("Retrieving Prices, Please Wait");
        progressDialog.show();

        databaseReference.child("price").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String typeAprice = dataSnapshot.child("oil1").getValue().toString();
                final String typeBprice = dataSnapshot.child("oil2").getValue().toString();
                final String typeCprice = dataSnapshot.child("oil3").getValue().toString();
                final String typeDprice = dataSnapshot.child("oil4").getValue().toString();

                type1tv.setText(typeAprice+"/litre");
                type2tv.setText(typeBprice+"/litre");
                type3tv.setText(typeCprice+"/litre");
                type4tv.setText(typeDprice+"/litre");
                progressDialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }
}
