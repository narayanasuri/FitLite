package koolkat.fitlite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 4/16/2017.
 */

public class AdminOrderViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private AdminOrderViewCustomAdapter adapter;

    final private List<String> oilTypes = new ArrayList<String>();
    final private List<Integer> quantities = new ArrayList<Integer>();
    final private List<Integer> prices = new ArrayList<Integer>();

    private final List<OilRequest> oilRequestsList = new ArrayList<>();
    private String username;

    public AdminOrderViewActivity() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);

        recyclerView = (RecyclerView) findViewById(R.id.admin_order_recycler_view);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        username = getIntent().getStringExtra("username");

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new AdminOrderViewCustomAdapter(oilTypes, quantities, prices);

        recyclerView.setAdapter(adapter);

        ViewCompat.setNestedScrollingEnabled(recyclerView, true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                oilTypes.clear();
                quantities.clear();
                String uid = dataSnapshot.child("userids").child(username).getValue().toString();
                Iterable<DataSnapshot> oilInformation = dataSnapshot.child("requests").child(uid).child("orders").getChildren();
                for(DataSnapshot info : oilInformation){
                    OilRequest oilRequest = info.getValue(OilRequest.class);
                    oilRequestsList.add(oilRequest);
                    oilTypes.add(oilRequest.getOilType());
                    quantities.add(oilRequest.getOilQuantity());
                    prices.add(oilRequest.getPrice());

                    recyclerView.setHasFixedSize(true);

                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    adapter = new AdminOrderViewCustomAdapter(oilTypes, quantities, prices);

                    recyclerView.setAdapter(adapter);

                    ViewCompat.setNestedScrollingEnabled(recyclerView, true);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
