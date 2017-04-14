package koolkat.fitlite;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    String oiltype;
    String orderid;
    public String status = "Pending";
    int quantity;
    public int reqId;
    Button requestButton;
    EditText quantityet;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

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

        progressDialog = new ProgressDialog(getContext());
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        adapter = ArrayAdapter.createFromResource(getContext(), R.array.oiltypes, R.layout.spinner_item);
        adapter .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                oiltype = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        requestButton.setOnClickListener(this);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child("requests").child(user.getUid()).child("orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               reqId = (int)dataSnapshot.getChildrenCount();
                Log.d("ORDER ID", reqId + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v==requestButton) {
            progressDialog.setMessage("Requesting");
            progressDialog.show();

            String quantitystr = quantityet.getText().toString();
            if (!TextUtils.isEmpty(quantitystr)) {
                quantity = Integer.parseInt(quantityet.getText().toString());
                if (quantity > 0) {
                    a(reqId);
                }
            }

            else {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Request Failed! Please try again!", Toast.LENGTH_SHORT).show();
            }
            quantityet.setText("");
        }
    }

    public void a(int reqId) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        reqId = reqId + 1;
        orderid = "order" + reqId;
        OilRequest oilRequest = new OilRequest(reqId, oiltype, quantity, status);
        databaseReference.child("requests").child(user.getUid()).child("numberOfOrders").setValue(reqId);
        databaseReference.child("requests").child(user.getUid()).child("orders").child(orderid).setValue(oilRequest);
        progressDialog.dismiss();
        Toast.makeText(getContext(), "Request successful!", Toast.LENGTH_SHORT).show();

    }

}
