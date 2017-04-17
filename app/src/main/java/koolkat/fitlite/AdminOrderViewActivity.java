package koolkat.fitlite;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class AdminOrderViewActivity extends Activity {

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
    private String phone;
    String uid;

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
        phone = getIntent().getStringExtra("phone");
        TextView a = (TextView) findViewById(R.id.name1);
        TextView b = (TextView) findViewById(R.id.phone1);
        a.setText("Name : " + username);
        b.setText("Mobile : " + phone);

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
                uid = dataSnapshot.child("userids").child(username).getValue().toString();
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

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                final View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    final int i = rv.getChildAdapterPosition(child);

                    AlertDialog.Builder a_builder = new AlertDialog.Builder(AdminOrderViewActivity.this);
                    a_builder.setMessage("Approve this order?")
                            .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    databaseReference.child("requests").child(uid).child("orders").child(i+"").child("status").setValue("Approved");
                                    Toast.makeText(getApplicationContext(), "Order Approved!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    databaseReference.child("requests").child(uid).child("orders").child(i+"").child("status").setValue("Denied");
                                    Toast.makeText(getApplicationContext(), "Order Denied!", Toast.LENGTH_SHORT).show();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Order Confirmation");
                    alert.show();

                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }

}
