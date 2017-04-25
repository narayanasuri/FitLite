package koolkat.fitlite;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 4/16/2017.
 */

public class AdminconOrderViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private AdminconOrderViewCustomAdapter adapter;

    final private List<String> oilTypes = new ArrayList<String>();
    final private List<Integer> quantities = new ArrayList<Integer>();
    final private List<Integer> prices = new ArrayList<Integer>();
    final private List<String> status = new ArrayList<String>();
    final private List<String> dates = new ArrayList<String>();

    private final List<OilRequest> oilRequestsList = new ArrayList<>();
    private String username;
    private String phone;
    String uid;
    FloatingActionButton callfab;

    public AdminconOrderViewActivity() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);

        recyclerView = (RecyclerView) findViewById(R.id.admin_order_recycler_view);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        username = getIntent().getStringExtra("username");
        phone = getIntent().getStringExtra("phone");
        //TextView a = (TextView) findViewById(R.id.name1);
        //a.setText("Name : " + username);
        getSupportActionBar().setTitle(username);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        callfab = (FloatingActionButton) findViewById(R.id.orderviewcallfab);

        adapter = new AdminconOrderViewCustomAdapter(oilTypes, quantities, prices, status, dates);

        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    callfab.hide();
                else if (dy < 0)
                    callfab.show();
            }
        });

        ViewCompat.setNestedScrollingEnabled(recyclerView, true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                oilTypes.clear();
                quantities.clear();
                prices.clear();
                status.clear();
                dates.clear();
                uid = dataSnapshot.child("userids").child(username).getValue().toString();
                Iterable<DataSnapshot> oilInformation = dataSnapshot.child("requests").child(uid).child("orders").getChildren();
                for (DataSnapshot info : oilInformation) {
                    OilRequest oilRequest = info.getValue(OilRequest.class);
                    oilRequestsList.add(oilRequest);
                    if (oilRequest.getStatus().equals("Approved")) {
                        oilTypes.add(oilRequest.getOilType());
                        quantities.add(oilRequest.getOilQuantity());
                        prices.add(oilRequest.getPrice());
                        status.add(oilRequest.getStatus());
                        dates.add(oilRequest.getOrderdate());
                        recyclerView.setHasFixedSize(true);

                        layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        adapter = new AdminconOrderViewCustomAdapter(oilTypes, quantities, prices, status, dates);

                        recyclerView.setAdapter(adapter);

                        ViewCompat.setNestedScrollingEnabled(recyclerView, true);

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        callfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phone));
                startActivity(intent);
            }
        });

    }

}
