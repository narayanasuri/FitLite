package koolkat.fitlite;

import android.app.ProgressDialog;
import android.content.Intent;
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
    EditText type1tv, type2tv, type3tv, type4tv;
    Button edit;
    String typeAprice,typeBprice,typeCprice,typeDprice;
    int x=1;

    public AdminOrderFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_order, container, false);
        type1tv = (EditText) view.findViewById(R.id.oiltypeAtv);
        type2tv = (EditText) view.findViewById(R.id.oiltypeBtv);
        type3tv = (EditText) view.findViewById(R.id.oiltypeCtv);
        type4tv = (EditText) view.findViewById(R.id.oiltypeDtv);
        type1tv.setEnabled(false);type2tv.setEnabled(false);type3tv.setEnabled(false);type4tv.setEnabled(false);
        edit=(Button) view.findViewById(R.id.editbutton);
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

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(x%2==1){
                    edit.setText("Save");
                    type1tv.setEnabled(true);type2tv.setEnabled(true);type3tv.setEnabled(true);type4tv.setEnabled(true);
                    type1tv.setText(typeAprice);
                    type2tv.setText(typeBprice);
                    type3tv.setText(typeCprice);
                    type4tv.setText(typeDprice);
                    type1tv.setInputType(InputType.TYPE_CLASS_NUMBER);
                    type2tv.setInputType(InputType.TYPE_CLASS_NUMBER);
                    type3tv.setInputType(InputType.TYPE_CLASS_NUMBER);
                    type4tv.setInputType(InputType.TYPE_CLASS_NUMBER);
                    Toast.makeText(getContext(), "Please edit the details", Toast.LENGTH_SHORT).show();
                    x++;
                }
                else{
                    progressDialog.setMessage("Submitting Prices, Please Wait");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    type1tv.setInputType(InputType.TYPE_CLASS_TEXT);
                    type2tv.setInputType(InputType.TYPE_CLASS_TEXT);
                    type3tv.setInputType(InputType.TYPE_CLASS_TEXT);
                    type4tv.setInputType(InputType.TYPE_CLASS_TEXT);
                    type1tv.setEnabled(false);type2tv.setEnabled(false);type3tv.setEnabled(false);type4tv.setEnabled(false);
                    typeAprice=type1tv.getText().toString();
                    typeBprice=type2tv.getText().toString();
                    typeCprice=type3tv.getText().toString();
                    typeDprice=type4tv.getText().toString();
                    databaseReference.child("price").child("oil1").setValue(typeAprice);
                    databaseReference.child("price").child("oil2").setValue(typeBprice);
                    databaseReference.child("price").child("oil3").setValue(typeCprice);
                    databaseReference.child("price").child("oil4").setValue(typeDprice);
                    edit.setText("Edit");
                    type1tv.setText(typeAprice+"/litre");
                    type2tv.setText(typeBprice+"/litre");
                    type3tv.setText(typeCprice+"/litre");
                    type4tv.setText(typeDprice+"/litre");
                    x++;
                    Toast.makeText(getContext(), "Data Saved", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                }
            }
        });
        return view;
    }

}
