package koolkat.fitlite;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class AdminOrderViewActivity extends AppCompatActivity {

    final private List<String> oilTypes = new ArrayList<>();
    final private List<Integer> quantities = new ArrayList<>();
    final private List<Integer> prices = new ArrayList<>();
    final private List<Integer> request = new ArrayList<>();
    final private List<Integer> discount = new ArrayList<>();
    final private List<String> statuses = new ArrayList<>();
    final private List<String> dates = new ArrayList<>();
    private final List<OilRequest> oilRequestsList = new ArrayList<>();
    private int confirmed;
    private int litres;
    private int x;
    private int price;
    private int oilQuantity;
    private int count;
    private String uid;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private AdminOrderViewCustomAdapter adapter;
    private String username;
    private String phone;
    private FloatingActionButton callfab;


    public AdminOrderViewActivity() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);

        recyclerView = (RecyclerView) findViewById(R.id.admin_order_recycler_view);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        username = getIntent().getStringExtra("username");
        phone = getIntent().getStringExtra("phone");
        //TextView a = (TextView) findViewById(R.id.name1);
        //a.setText("Name : " + username);
        getSupportActionBar().setTitle(username+"'s Orders");
        callfab = (FloatingActionButton) findViewById(R.id.orderviewcallfab);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new AdminOrderViewCustomAdapter(oilTypes, quantities, prices, statuses, dates);

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

        b();
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

                                    final int pos = request.get(i);
                                    count = 0;
                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (count == 0) {
                                                databaseReference.child("requests").child(uid).child("orders").child(pos + "").child("status").setValue("Approved");
                                                confirmed = Integer.parseInt(dataSnapshot.child("calc").child(uid).child("confirmedOrders").getValue().toString());
                                                litres = Integer.parseInt(dataSnapshot.child("calc").child(uid).child("numberOfLitres").getValue().toString());
                                                x = Integer.parseInt(dataSnapshot.child("calc").child(uid).child("price").getValue().toString());
                                                price = Integer.parseInt(dataSnapshot.child("requests").child(uid).child("orders").child(pos + "").child("price").getValue().toString());
                                                oilQuantity = Integer.parseInt(dataSnapshot.child("requests").child(uid).child("orders").child(pos + "").child("oilQuantity").getValue().toString());
                                                a(confirmed, litres, x, price, oilQuantity, pos);
                                                count++;
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    b();
                                    adapter.notifyDataSetChanged();
                                    count = 0;
                                }
                            })
                            .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final int pos = request.get(i);
                                    databaseReference.child("requests").child(uid).child("orders").child(pos + "").child("status").setValue("Denied");
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

        callfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phone));
                startActivity(intent);
            }
        });

    }

    private void a(int confirmed, int litres, int x, int price, int oilQuantity, int pos) {
        confirmed += 1;
        databaseReference.child("calc").child(uid).child("confirmedOrders").setValue(confirmed);
        litres += oilQuantity;
        databaseReference.child("calc").child(uid).child("numberOfLitres").setValue(litres);
        int y = litres / 26;
        y -= x;
        int a = price / oilQuantity;
        price = ((oilQuantity - y) * a) + (y * 15);
        databaseReference.child("requests").child(uid).child("orders").child(pos + "").child("discount").setValue(y);
        databaseReference.child("requests").child(uid).child("orders").child(pos + "").child("price").setValue(price);
        databaseReference.child("calc").child(uid).child("price").setValue(x + y);
        Toast.makeText(getApplicationContext(), "Order Approved!", Toast.LENGTH_SHORT).show();
    }

    private void b() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                request.clear();
                oilTypes.clear();
                quantities.clear();
                prices.clear();
                statuses.clear();
                discount.clear();
                dates.clear();
                uid = dataSnapshot.child("userids").child(username).getValue().toString();
                Iterable<DataSnapshot> oilInformation = dataSnapshot.child("requests").child(uid).child("orders").getChildren();
                for (DataSnapshot info : oilInformation) {
                    OilRequest oilRequest = info.getValue(OilRequest.class);
                    oilRequestsList.add(oilRequest);
                    if (oilRequest.getStatus().equals("Pending") || oilRequest.getStatus().equals("Denied")) {
                        oilTypes.add(oilRequest.getOilType());
                        request.add(oilRequest.getRequestId());
                        quantities.add(oilRequest.getOilQuantity());
                        prices.add(oilRequest.getPrice());
                        statuses.add(oilRequest.getStatus());
                        discount.add(oilRequest.getDiscount());
                        dates.add(oilRequest.getOrderdate());
                        recyclerView.setHasFixedSize(true);

                        layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        adapter = new AdminOrderViewCustomAdapter(oilTypes, quantities, prices, statuses, dates);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        ViewCompat.setNestedScrollingEnabled(recyclerView, true);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
