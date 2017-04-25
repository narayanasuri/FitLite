package koolkat.fitlite;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by Admin on 4/13/2017.
 */

public class AdminOrderFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    TextView type1tv, type2tv, type3tv, type4tv;
    EditText type1et, type2et, type3et, type4et;
    Button edit;
    String typeAprice, typeBprice, typeCprice, typeDprice;
    int x = 1;

    public AdminOrderFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_order, container, false);
        type1tv = (TextView) view.findViewById(R.id.oiltypeAtv);
        type2tv = (TextView) view.findViewById(R.id.oiltypeBtv);
        type3tv = (TextView) view.findViewById(R.id.oiltypeCtv);
        type4tv = (TextView) view.findViewById(R.id.oiltypeDtv);
        type1et = (EditText) view.findViewById(R.id.oiltypeAet);
        type2et = (EditText) view.findViewById(R.id.oiltypeBet);
        type3et = (EditText) view.findViewById(R.id.oiltypeCet);
        type4et = (EditText) view.findViewById(R.id.oiltypeDet);
        type1et.setEnabled(false);
        type2et.setEnabled(false);
        type3et.setEnabled(false);
        type4et.setEnabled(false);
        edit = (Button) view.findViewById(R.id.editbutton);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setMessage("Retrieving Prices, Please Wait");
        progressDialog.show();
        progressDialog.setCancelable(false);

        databaseReference.child("price").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                typeAprice = dataSnapshot.child("oil1").getValue().toString();
                typeBprice = dataSnapshot.child("oil2").getValue().toString();
                typeCprice = dataSnapshot.child("oil3").getValue().toString();
                typeDprice = dataSnapshot.child("oil4").getValue().toString();

                type1tv.setText(typeAprice + "/litre");
                type2tv.setText(typeBprice + "/litre");
                type3tv.setText(typeCprice + "/litre");
                type4tv.setText(typeDprice + "/litre");
                type1et.setText(typeAprice + "/litre");
                type2et.setText(typeBprice + "/litre");
                type3et.setText(typeCprice + "/litre");
                type4et.setText(typeDprice + "/litre");
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (x % 2 == 1) {
                    edit.setText("Save");
                    type1et.setEnabled(true);
                    type2et.setEnabled(true);
                    type3et.setEnabled(true);
                    type4et.setEnabled(true);
                    type1tv.setText(typeAprice);
                    type2tv.setText(typeBprice);
                    type3tv.setText(typeCprice);
                    type4tv.setText(typeDprice);
                    type1et.setText(typeAprice);
                    type2et.setText(typeBprice);
                    type3et.setText(typeCprice);
                    type4et.setText(typeDprice);
                    type1et.setInputType(InputType.TYPE_CLASS_NUMBER);
                    type2et.setInputType(InputType.TYPE_CLASS_NUMBER);
                    type3et.setInputType(InputType.TYPE_CLASS_NUMBER);
                    type4et.setInputType(InputType.TYPE_CLASS_NUMBER);
                    Toast.makeText(getContext(), "Please edit the details", Toast.LENGTH_SHORT).show();
                    x++;
                } else {
                    progressDialog.setMessage("Submitting Prices, Please Wait");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    type1et.setInputType(InputType.TYPE_CLASS_TEXT);
                    type2et.setInputType(InputType.TYPE_CLASS_TEXT);
                    type3et.setInputType(InputType.TYPE_CLASS_TEXT);
                    type4et.setInputType(InputType.TYPE_CLASS_TEXT);
                    type1et.setEnabled(false);
                    type2et.setEnabled(false);
                    type3et.setEnabled(false);
                    type4et.setEnabled(false);
                    typeAprice = type1et.getText().toString();
                    typeBprice = type2et.getText().toString();
                    typeCprice = type3et.getText().toString();
                    typeDprice = type4et.getText().toString();
                    databaseReference.child("price").child("oil1").setValue(typeAprice);
                    databaseReference.child("price").child("oil2").setValue(typeBprice);
                    databaseReference.child("price").child("oil3").setValue(typeCprice);
                    databaseReference.child("price").child("oil4").setValue(typeDprice);
                    edit.setText("Edit");
                    type1tv.setText(typeAprice + "/litre");
                    type2tv.setText(typeBprice + "/litre");
                    type3tv.setText(typeCprice + "/litre");
                    type4tv.setText(typeDprice + "/litre");
                    x++;
                    Toast.makeText(getContext(), "Data Saved", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
        return view;
    }

}
