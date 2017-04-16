package koolkat.fitlite;

/**
 * Created by Admin on 4/8/2017.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnLogin, btnforgotPassword;
    TextView registertv, forgotPasswordtv, nvmtv;
    EditText emailet, passwordet, forgotEmailet;
    CardView loginCard, forgotPasswordCard;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        loginCard = (CardView) findViewById(R.id.login_layout);
        forgotPasswordCard = (CardView) findViewById(R.id.forgotpassword_layout);
        registertv = (TextView) findViewById(R.id.signup_tv);
        forgotPasswordtv = (TextView) findViewById(R.id.forgotpasswordtv);
        nvmtv = (TextView) findViewById(R.id.nvmtv);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnforgotPassword = (Button) findViewById(R.id.btnforgotPassword);
        forgotEmailet = (EditText) findViewById(R.id.forgotemailidet);
        emailet = (EditText) findViewById(R.id.emailidet);
        passwordet = (EditText) findViewById(R.id.passwordet);
        registertv.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        nvmtv.setOnClickListener(this);
        forgotPasswordtv.setOnClickListener(this);
        btnforgotPassword.setOnClickListener(this);

        if(firebaseAuth.getCurrentUser() != null){
            finish();
            String email = firebaseAuth.getCurrentUser().getEmail();
            if(email.equalsIgnoreCase("narayanasuri08@gmail.com")){
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(intent);
            }
        }
    }

    private void userLogin(){
        final String email = emailet.getText().toString().trim();
        String pwd = passwordet.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your email id!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(pwd)){
            Toast.makeText(this, "Please enter your password!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Signing In");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       progressDialog.dismiss();

                        if(task.isSuccessful()){
                            if(email.equalsIgnoreCase("narayanasuri08@gmail.com")){
                                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                                Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(intent);
                            }
                            else {
                                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                                Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(intent);
                            }
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Login Failed! Please try again!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v==registertv)
            registerAccount();

        if(v==btnLogin){
            userLogin();
        }

        if(v==forgotPasswordtv){
            registertv.setVisibility(View.GONE);
            loginCard.setVisibility(View.GONE);
            forgotPasswordCard.setVisibility(View.VISIBLE);
        }

        if(v==nvmtv){
            forgotPasswordCard.setVisibility(View.GONE);
            loginCard.setVisibility(View.VISIBLE);
            registertv.setVisibility(View.VISIBLE);
        }

        if(v==btnforgotPassword){
            forgotPasswordCard.setVisibility(View.GONE);
            loginCard.setVisibility(View.VISIBLE);
            registertv.setVisibility(View.VISIBLE);
            String email = forgotEmailet.getText().toString();
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "An email has been sent to you!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Something bad happened! Please try again!", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private void registerAccount(){
        finish();
        startActivity(new Intent(this, RegisterActivity.class));
    }
}