package koolkat.fitlite;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameet;
    private EditText phnoet;
    private EditText emailidet;
    private EditText pwd1et;
    private EditText pwd2et;
    private EditText referret;
    private Button btnRegister;
    private TextView logintv;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = database.getReference();

        usernameet = (EditText) findViewById(R.id.usernameet);
        phnoet = (EditText) findViewById(R.id.phonenumet);
        emailidet = (EditText) findViewById(R.id.emailet);
        pwd1et = (EditText) findViewById(R.id.pwd1et);
        pwd2et = (EditText) findViewById(R.id.pwd2et);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        logintv = (TextView) findViewById(R.id.signin_tv);
        referret =(EditText) findViewById(R.id.referralet);
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

        final String name = usernameet.getText().toString().trim();
        final String phonenumber = phnoet.getText().toString().trim();
        String email = emailidet.getText().toString().trim();
        String pwd1 = pwd1et.getText().toString().trim();
        String pwd2 = pwd2et.getText().toString().trim();
        String referred=referret.getText().toString().trim();
        final String[] referredid = {""};
        final String[] referral = {GenerateRandomString.randomString(6)};
        if (referred.equals(""))
            referred="A";

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd1) || TextUtils.isEmpty(pwd2)) {
            Toast.makeText(this, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pwd1.equals(pwd2)) {
            progressDialog.setMessage("Registering User");
            progressDialog.show();

            final String finalReferred = referred;
            firebaseAuth.createUserWithEmailAndPassword(email, pwd1)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                final FirebaseUser user = firebaseAuth.getCurrentUser();
                                databaseReference.child("Referralcodes").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.hasChild(finalReferred)){
                                            referredid[0] =dataSnapshot.child(finalReferred).getValue().toString();
                                        }
                                        int flag=0;
                                        while(flag==0)
                                        {
                                            if (dataSnapshot.hasChild(referral[0])){
                                                referral[0]=GenerateRandomString.randomString(6);
                                            }
                                            else
                                                flag=1;
                                        }
                                        UserInformation userInformation = new UserInformation(0,name,phonenumber,referral[0], referredid[0],0,0,user.getUid(),0);
                                        databaseReference.child("Users").child(user.getUid()).setValue(userInformation);
                                        databaseReference.child("Referralcodes").child(referral[0]).setValue(user.getUid());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


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
class GenerateRandomString {

    public static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static Random RANDOM = new Random();

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return sb.toString();
    }

}