package com.example.ema.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ema.R;
import com.example.ema.classs.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Menu extends AppCompatActivity {
    private Button make1007FormBtn, watchListBtn, historyBtn, waitingConfirmationBtn;
    private TextView textView;
    public FirebaseUser firebaseUser;
    public static User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //English default:
        Configuration config = new Configuration();
        Locale locale = new Locale("heb");

        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        make1007FormBtn = (Button) findViewById(R.id.new_form);
        make1007FormBtn.setVisibility(View.GONE);
        watchListBtn = (Button) findViewById(R.id.table_tracking);
        watchListBtn.setVisibility(View.GONE);
        historyBtn = (Button) findViewById(R.id.history);
        waitingConfirmationBtn = findViewById(R.id.waitingConfirmation);
        waitingConfirmationBtn.setVisibility(View.GONE);
        textView = findViewById(R.id.name);

        if (firebaseUser != null) {
            String email = firebaseUser.getEmail();
            String hello = null;
            user = new User();


            if (email.equals("kanehhh777@gmail.com")) {
                user.setName("משה יעקב");
                user.setDarga("אעצ");
                user.setJobTitle("מנהל");
                user.setId("7663944");
                user.setEmail(email);

            } else if (email.equals("refael2412@gmail.com")) {
                user.setName("רפאל טיאר");
                user.setDarga("רסמ");
                user.setId("654755");
                user.setEmail(email);
                user.setJobTitle("בוחן");

            } else if (email.equals("liorr00300@gmail.com")) {
                user.setName("ליאור אינייב");
                user.setDarga("אעצ");
                user.setId("9806106");
                user.setEmail(email);
                user.setJobTitle("בוחן");

            } else if (email.equals("Amim0379@gmail.com")) {
                user.setName("עמי מכלוף");
                user.setDarga("רסב");
                user.setId("5262556");
                user.setEmail(email);
                user.setJobTitle("מנהל");

            }
            if (user.getJobTitle().equals("בוחן")) {
                make1007FormBtn.setVisibility(View.VISIBLE);
                watchListBtn.setVisibility(View.VISIBLE);


            } else if (user.getJobTitle().equals("מנהל")) {
                waitingConfirmationBtn.setVisibility(View.VISIBLE);
            }


            String currentTime = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());

            if (Integer.valueOf(currentTime) < 12 && Integer.valueOf(currentTime) >= 6) {
                hello = "  בוקר טוב ";
            } else if (Integer.valueOf(currentTime) >= 12 && Integer.valueOf(currentTime) <= 18) {
                hello = "  צהריים טובים ";
            } else if (Integer.valueOf(currentTime) > 18 && Integer.valueOf(currentTime) < 21) {
                hello = "  ערב טוב ";

            } else
                hello = "  לילה טוב ";

            textView.setText(hello + "\n" + user.getName());
        }

        make1007FormBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, MakeForm.class);
                startActivity(intent);
            }
        });
        watchListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, WatchList.class);
                startActivity(intent);
            }
        });
        waitingConfirmationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, ManagerWatchList.class);
                startActivity(intent);
            }
        });
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, History1007.class);
                startActivity(intent);
            }
        });


    }
}
