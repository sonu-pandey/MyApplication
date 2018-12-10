package com.example.rututechnologies.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public ImageView userProfile;
    private TextView tvCreateAcct;
    public TextView  tvLogin;
    private TextView tVForgot;
    private EditText etEmail;
    private EditText etPassword;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth= FirebaseAuth.getInstance();

        InitializeFields();

        tvCreateAcct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               SendUserToRegisterActivity();

            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               AllowUserToLogin();

            }
        });

    }

    private void AllowUserToLogin() {

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "All fileds are required", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(LoginActivity.this, "password must be at least 6 characters", Toast.LENGTH_SHORT).show();
        } else
        {

            loadingBar.setTitle("Sign In");
            loadingBar.setMessage("Please wait....");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
           mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            SendUserToMainActivity();
                            Toast.makeText(LoginActivity.this, "Logged In Succesfully..", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                        }else {
                            String message = task.getException().toString();
                            Toast.makeText(LoginActivity.this, "Error :" + message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }


                });

        }

    }

    private void InitializeFields() {

        tvCreateAcct=(TextView)findViewById(R.id.tvCreateAcct);
        userProfile=(ImageView)findViewById(R.id.userProfile);
        tvLogin=(TextView)findViewById(R.id.tvLogin);
        tVForgot=(TextView)findViewById(R.id.tvForgot);
        etEmail=(EditText)findViewById(R.id.etEmail);
        etPassword=(EditText)findViewById(R.id.etPassword);
        loadingBar= new ProgressDialog(this);

    }


    private void SendUserToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void SendUserToRegisterActivity() {

        Intent registerintent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerintent);
    }
}
