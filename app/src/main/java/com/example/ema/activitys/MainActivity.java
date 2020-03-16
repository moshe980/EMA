package com.example.ema.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ema.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private EditText emailTV, passwordTV;
    private Button loginBtn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private static final int STORGE_PERMISSION_CODE=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ,Manifest.permission.READ_EXTERNAL_STORAGE
                        ,Manifest.permission.READ_CONTACTS
                        ,Manifest.permission.INTERNET},
                STORGE_PERMISSION_CODE );

        //English default:
        Configuration config = new Configuration();
        Locale locale = new Locale("heb");

        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        mAuth = FirebaseAuth.getInstance();

        initializeUI();


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAccount();

            }
        });

    }

    private void loginUserAccount() {

        final String email, password;
        email = emailTV.getText().toString();
        password = passwordTV.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "הכנס איימל!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "הכנס סיסמא!", Toast.LENGTH_LONG).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "ההתחברות הצליחה!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            Intent intent=new Intent(MainActivity.this, Menu.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "החיבור כשל תנסה בפעם אחרת!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });
    }

    private void initializeUI() {
        emailTV = findViewById(R.id.email);
        passwordTV = findViewById(R.id.password);

        loginBtn = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}

