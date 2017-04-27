package koolkat.fitlite;


import android.app.ProgressDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment implements View.OnClickListener {

    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private String oiltype;
    private String orderid;
    private String status = "Pending";
    private int quantity;
    private int discount = 0;
    private int price = 0;
    private int Aprice;
    private int Bprice;
    private int Cprice;
    private int Dprice;
    private int reqId;
    private Button requestButton;
    private EditText quantityet;
    private TextView pricea;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    //private Calendar c;
    private java.util.Calendar c;
    private java.text.SimpleDateFormat df;
    private int value=0;
    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_request, container, false);
        spinner = (Spinner) view.findViewById(R.id.oiltypespinner);
        requestButton = (Button) view.findViewById(R.id.requestBtn);
        quantityet = (EditText) view.findViewById(R.id.quantity_et);
        pricea = (TextView) view.findViewById(R.id.price);

        df = new java.text.SimpleDateFormat("dd-MMM-yyyy");
        c = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT+5:30"));
        //c = Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT+5:30"));

        progressDialog = new ProgressDialog(getContext());
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        adapter = ArrayAdapter.createFromResource(getContext(), R.array.oiltypes, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        requestButton.setOnClickListener(this);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child("price").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Aprice = Integer.parseInt(dataSnapshot.child("oil1").getValue().toString());
                Bprice = Integer.parseInt(dataSnapshot.child("oil2").getValue().toString());
                Cprice = Integer.parseInt(dataSnapshot.child("oil3").getValue().toString());
                Dprice = Integer.parseInt(dataSnapshot.child("oil4").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("requests").child(user.getUid()).child("orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                reqId = (int) dataSnapshot.getChildrenCount();
                Log.d("ORDER ID", reqId + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                oiltype = parent.getItemAtPosition(position).toString();
                value=position;
                b();
                pricea.setText("₹" + price + "/-");
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        quantityet.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                b();
            }

            @Override
            public void afterTextChanged(Editable s) {
                pricea.setText("₹" + price + "/-");
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == requestButton) {
            progressDialog.setMessage("Requesting");
            progressDialog.show();

            String quantitystr = quantityet.getText().toString();
            if (!TextUtils.isEmpty(quantitystr)) {
                quantity = Integer.parseInt(quantityet.getText().toString());
                if (quantity > 0) {
                    a(reqId);
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Request Failed! Please try again!", Toast.LENGTH_SHORT).show();
            }
            quantityet.setText("");
        }
    }

    private void a(int reqId) {
        String formattedDate = df.format(c.getTime());
        FirebaseUser user = firebaseAuth.getCurrentUser();
        reqId = reqId + 1;
        orderid = "" + reqId;
        OilRequest oilRequest = new OilRequest(reqId, oiltype, quantity, status, price, discount, formattedDate);
        databaseReference.child("Users").child(user.getUid()).child("numberOfOrders").setValue(reqId);
        databaseReference.child("requests").child(user.getUid()).child("numberOfOrders").setValue(reqId);
        databaseReference.child("requests").child(user.getUid()).child("orders").child(orderid).setValue(oilRequest);
        progressDialog.dismiss();
        Toast.makeText(getContext(), "Request successful!", Toast.LENGTH_SHORT).show();
    }

    private void b() {
        String alpha = quantityet.getText().toString();
        int x;
        if (oiltype == null)
            oiltype = "";
        if (alpha.equals(""))
            x = 0;
        else
            x = Integer.parseInt(alpha);
        switch (value) {
            case 0:
                price = x * Aprice;
                break;
            case 1:
                price = x * Bprice;
                break;
            case 2:
                price = x * Cprice;
                break;
            case 3:
                price = x * Dprice;
                break;
            default:
                price = 0;
        }
    }
}
