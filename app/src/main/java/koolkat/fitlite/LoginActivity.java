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
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnRegisterAct, btnLogin;
    EditText emailet, passwordet;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        btnRegisterAct = (Button) findViewById(R.id.btnRegisterActivity);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        emailet = (EditText) findViewById(R.id.emailidet);
        passwordet = (EditText) findViewById(R.id.passwordet);
        btnRegisterAct.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, UserActivity.class));
        }
    }

    private void userLogin(){
        String email = emailet.getText().toString().trim();
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
                            finish();
                            Toast.makeText(getApplicationContext(), "Sign In Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), UserActivity.class));
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Login Failed! Please try again!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v==btnRegisterAct)
            registerAccount();

        if(v==btnLogin){
            userLogin();
        }
    }

    private void registerAccount(){
        finish();
        startActivity(new Intent(this, RegisterActivity.class));
    }
}