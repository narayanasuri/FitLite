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
import android.support.v4.content.ContextCompat;
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
import android.widget.Button;
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
    final private List<String> statuses = new ArrayList<String>();

    private final List<OilRequest> oilRequestsList = new ArrayList<>();
    private String username;
    private String phone;
    String uid;
    int x,confirmed,litres,price,oilQuantity;


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

        adapter = new AdminOrderViewCustomAdapter(oilTypes, quantities, prices, statuses);

        recyclerView.setAdapter(adapter);

        ViewCompat.setNestedScrollingEnabled(recyclerView, true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                oilTypes.clear();
                quantities.clear();
                prices.clear();
                statuses.clear();
                uid = dataSnapshot.child("userids").child(username).getValue().toString();
                Iterable<DataSnapshot> oilInformation = dataSnapshot.child("requests").child(uid).child("orders").getChildren();
                for(DataSnapshot info : oilInformation){
                    OilRequest oilRequest = info.getValue(OilRequest.class);
                    oilRequestsList.add(oilRequest);
                    if(oilRequest.getStatus().equals("Pending")||oilRequest.getStatus().equals("Denied")){
                    oilTypes.add(oilRequest.getOilType());
                    quantities.add(oilRequest.getOilQuantity());
                    prices.add(oilRequest.getPrice());
                    statuses.add(oilRequest.getStatus());

                    recyclerView.setHasFixedSize(true);

                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    adapter = new AdminOrderViewCustomAdapter(oilTypes, quantities, prices, statuses);

                    recyclerView.setAdapter(adapter);

                    ViewCompat.setNestedScrollingEnabled(recyclerView, true);}

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
                                    int pos = i+1;
                                    databaseReference.child("requests").child(uid).child("orders").child(pos+"").child("status").setValue("Approved");
                                    databaseReference.child("calc").child(uid).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                           confirmed = Integer.parseInt( dataSnapshot.child("confirmedOrders").getValue().toString());
                                           litres = Integer.parseInt( dataSnapshot.child("numberOfLitres").getValue().toString());
                                           x = Integer.parseInt( dataSnapshot.child("price").getValue().toString());
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    databaseReference.child("requests").child(uid).child("orders").child(pos+"").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            price = Integer.parseInt( dataSnapshot.child("price").getValue().toString());
                                            oilQuantity=Integer.parseInt( dataSnapshot.child("oilQuantity").getValue().toString());
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    confirmed+=1;
                                    databaseReference.child("calc").child(uid).child("confirmedOrders").setValue(""+confirmed);
                                    litres+=oilQuantity;
                                    databaseReference.child("calc").child(uid).child("numberOfLitres").setValue(""+litres);
                                    int y=litres/26;
                                    y-=x;
                                    int a=price/oilQuantity;
                                    price=((oilQuantity-y)*a)+(y*15);
                                    databaseReference.child("requests").child(uid).child("orders").child(pos+"").child("price").setValue(""+price);
                                    databaseReference.child("calc").child(uid).child("price").setValue(""+x+y);
                                    Toast.makeText(getApplicationContext(), "Order Approved!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int pos = i+1;
                                    databaseReference.child("requests").child(uid).child("orders").child(pos+"").child("status").setValue("Denied");
                                    Toast.makeText(getApplicationContext(), "Order Denied!", Toast.LENGTH_SHORT).show();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Order Confirmation");
                    alert.show();
                    Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                    nbutton.setTextColor(ContextCompat.getColor(AdminOrderViewActivity.this, R.color.colorPrimaryDark));
                    Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                    pbutton.setTextColor(ContextCompat.getColor(AdminOrderViewActivity.this, R.color.colorPrimaryDark));

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
