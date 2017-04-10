package koolkat.fitlite;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    UserInformation userInfo;

    TextView welcometv;
    Button btnlogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcometv = (TextView) findViewById(R.id.welcometv);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                final UserInformation userInformation = dataSnapshot.child("Users").child(user.getUid()).getValue(UserInformation.class);
                final String username = userInformation.getUsername();
                final String phonenumber = userInformation.getPhonenumber();
                userInfo = new UserInformation(username, phonenumber);
                welcometv.setText("Welcome "+userInfo.getUsername()+"!");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnlogout = (Button) findViewById(R.id.btnLogout);

        btnlogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==btnlogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
