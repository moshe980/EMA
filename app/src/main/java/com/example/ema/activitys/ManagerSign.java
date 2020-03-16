package com.example.ema.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ema.R;
import com.example.ema.classs.AdapterBitmap;
import com.example.ema.classs.Form1007;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ManagerSign extends AppCompatActivity {
    private Button saveBtn;
    private Context context;
    private DatabaseReference mDatabaseRef;
    private FirebaseDatabase database;
    private Form1007 current1007;
    private SignaturePad signaturePad;
    private Button saveButton, clearButton;
    private Bitmap finishUserSignature;
    private ProgressBar progressBar;
    private boolean flag=false;
    private boolean flag2=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_sign);

        context = this;

        //English default:
        Configuration config = new Configuration();
        Locale locale = new Locale("heb");

        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        Intent i = getIntent();
        current1007 = (Form1007) i.getSerializableExtra("sampleObject");

        progressBar = findViewById(R.id.progressBar5);
        saveBtn = findViewById(R.id.save);
        signaturePad = (SignaturePad) findViewById(R.id.signaturePad);
        saveButton = (Button) findViewById(R.id.saveButton);
        clearButton = (Button) findViewById(R.id.clearButton);

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                saveButton.setEnabled(true);
                clearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                saveButton.setEnabled(false);
                clearButton.setEnabled(false);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishUserSignature = signaturePad.getTransparentSignatureBitmap();
                signaturePad.setEnabled(false);
                saveButton.setEnabled(false);
                Toast.makeText(context, "החתימה נשמרה", Toast.LENGTH_SHORT).show();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signaturePad.clear();
                signaturePad.setEnabled(true);
                saveButton.setEnabled(true);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();

                current1007.setFinishUserId(Menu.user.getId());
                current1007.setFinishUserDarga(Menu.user.getDarga());
                current1007.setFinishUserName(Menu.user.getName());
                current1007.setFinishUserSignature(AdapterBitmap.adaptToString(finishUserSignature));
                current1007.setStatus("אושר");

                Date c = Calendar.getInstance().getTime();

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = df.format(c);
                current1007.setFinishDate(formattedDate);

                updateBalanceProcess();

                mDatabaseRef = database.getReference("users").child(current1007.getOpenUserEmail().substring(0, current1007.getOpenUserEmail()
                        .indexOf('@'))).child("closed1007");
                mDatabaseRef.child(String.valueOf(current1007.getId())).setValue(current1007);

                mDatabaseRef = database.getReference("users").child(current1007.getOpenUserEmail().substring(0, current1007.getOpenUserEmail()
                        .indexOf('@'))).child("opened1007");

                Query query = mDatabaseRef.orderByChild("equipmentId").equalTo(current1007.getEquipmentId());

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                saveBtn.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                saveButton.setEnabled(false);
                clearButton.setEnabled(false);

                Toast.makeText(getApplicationContext(), "העדכון בוצע בהצלחה", Toast.LENGTH_LONG).show();
                finish();
                current1007.makePdfForm(context);

            }


        });


    }




    public void updateBalanceProcess() {
        DatabaseReference myRef2 = database.getReference("users");
        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    for (DataSnapshot keyNode2 : keyNode.child("garages").getChildren()) {
                        for (DataSnapshot keyNode3 : keyNode2.child("orders").getChildren()) {
                            if (keyNode3.child("id").getValue().toString().equals(current1007.getOrderId())) {
                                int num1 = Integer.parseInt(current1007.getEndCost());
                                int tmp = Integer.valueOf(keyNode3.child("balance").getValue().toString());
                                int tmp2 = tmp - num1;

                                keyNode3.child("balance").getRef().setValue(String.valueOf(tmp2));
                                break;

                            }

                        }

                    }


                }

                flag2 = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
