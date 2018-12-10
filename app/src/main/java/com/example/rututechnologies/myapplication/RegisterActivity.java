package com.example.rututechnologies.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

public class RegisterActivity extends AppCompatActivity {

    public TextView tvCreate;
    private EditText etName;
    private EditText etMobile;
    private EditText etEmail1;
    private EditText etPassword1;
    private EditText etPassword2;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth= FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();

        InitializeFields();

        tvCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CreateNewAccount();

            }
        });
    }

    private void CreateNewAccount() {

        String email = etEmail1.getText().toString();
        String password = etPassword1.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "All fileds are required", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(RegisterActivity.this, "password must be at least 6 characters", Toast.LENGTH_SHORT).show();
        } else {

            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait, while we are creating new account for you");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                String currentUserID=mAuth.getCurrentUser().getUid();
                                RootRef.child("Users").child(currentUserID).setValue("");

                                SendUserToMainActivity();
                                Toast.makeText(RegisterActivity.this, "Account Crated Succesfully..", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Error :" + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }


                    });

        }
    }

    private void SendUserToMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private void InitializeFields() {

        etMobile = (EditText) findViewById(R.id.etMobile);
        etName = (EditText) findViewById(R.id.etName);
        etEmail1 = (EditText) findViewById(R.id.etEmail1);
        etPassword1 = (EditText) findViewById(R.id.etPassword1);
        etPassword2 = (EditText) findViewById(R.id.etPassword2);
        tvCreate = (TextView) findViewById(R.id.tvCreate);

        loadingBar = new ProgressDialog(this);

    }

}
