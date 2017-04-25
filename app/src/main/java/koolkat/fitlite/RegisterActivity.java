package koolkat.fitlite;

/**
 * Created by Admin on 4/8/2017.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText usernameet, phnoet, emailidet, pwd1et, pwd2et;
    Button btnRegister;
    TextView logintv;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);

        database = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = database.getReference();

        usernameet = (EditText) findViewById(R.id.usernameet);
        phnoet = (EditText) findViewById(R.id.phonenumet);
        emailidet = (EditText) findViewById(R.id.emailet);
        pwd1et = (EditText) findViewById(R.id.pwd1et);
        pwd2et = (EditText) findViewById(R.id.pwd2et);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        logintv = (TextView) findViewById(R.id.signin_tv);

        btnRegister.setOnClickListener(this);
        logintv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btnRegister) {
            registerUser();
        }
        if (v == logintv) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void registerUser() {

        final String username = usernameet.getText().toString().trim();
        final String phonenumber = phnoet.getText().toString().trim();
        String email = emailidet.getText().toString().trim();
        String pwd1 = pwd1et.getText().toString().trim();
        String pwd2 = pwd2et.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd1) || TextUtils.isEmpty(pwd2)) {
            Toast.makeText(this, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pwd1.equals(pwd2)) {
            progressDialog.setMessage("Registering User");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, pwd1)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                UserInformation userInformation = new UserInformation(0, username, phonenumber);
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                databaseReference.child("userids").child(username).setValue(user.getUid());
                                databaseReference.child("Users").child(user.getUid()).setValue(userInformation);
                                databaseReference.child("requests").child(user.getUid()).child("numberOfOrders").setValue(0);
                                databaseReference.child("calc").child(user.getUid()).child("numberOfLitres").setValue(0);
                                databaseReference.child("calc").child(user.getUid()).child("price").setValue(0);
                                databaseReference.child("calc").child(user.getUid()).child("confirmedOrders").setValue(0);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Registration Failed! Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else
            Toast.makeText(this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
    }

}