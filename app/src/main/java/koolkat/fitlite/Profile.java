package koolkat.fitlite;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    EditText name,mobile;
    TextView email;
    Button edit;
    String username,phone,femail,un;
    int x=1;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        name=(EditText) findViewById(R.id.name);
        mobile=(EditText) findViewById(R.id.mobile);
        email=(TextView) findViewById(R.id.email);
        name.setEnabled(false);
        mobile.setEnabled(false);
        edit=(Button) findViewById(R.id.editbutton);
        a();
        setData();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(x%2==1){
                    edit.setText("Save");
                    name.setInputType(InputType.TYPE_CLASS_TEXT);
                    name.setEnabled(true);
                    mobile.setInputType(InputType.TYPE_CLASS_PHONE);
                    mobile.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Please edit the details", Toast.LENGTH_SHORT).show();
                    x++;
                }
                else{
                    username=name.getText().toString();
                    phone=mobile.getText().toString();
                    databaseReference.child("userids").child(un).removeValue();
                    databaseReference.child("userids").child(username).setValue(user.getUid());
                    databaseReference.child("Users").child(user.getUid()).child("username").setValue(username);
                    databaseReference.child("Users").child(user.getUid()).child("phonenumber").setValue(phone);
                    edit.setText("Edit");
                    name.setEnabled(false);
                    mobile.setEnabled(false);
                    setData();
                    x++;
                    Toast.makeText(getApplicationContext(), "Data Saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void a()
    {
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        femail = user.getEmail();
        databaseReference.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("username").getValue().toString();
                un=username;
                phone = dataSnapshot.child("phonenumber").getValue().toString();name.setText(username);
                name.setText(username);
                mobile.setText(phone);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    public void setData()
    {
        name.setText(username);
        email.setText(femail);
        mobile.setText(phone);
    }
}
