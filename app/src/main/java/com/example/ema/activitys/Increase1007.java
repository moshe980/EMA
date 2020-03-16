package com.example.ema.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ema.R;
import com.example.ema.classs.AdapterBitmap;
import com.example.ema.classs.Form1007;
import com.example.ema.classs.Order;
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

public class Increase1007 extends AppCompatActivity {
    private EditText newCostET, newDeatailsET;
    private Button updateBtn;
    FirebaseDatabase database;
    DatabaseReference mDatabaseRef;
    Form1007 currentForm1007;
    private Button saveButton, clearButton;
    private Bitmap increaseUserSignature;
    private SignaturePad signaturePad;
    private boolean isSign;
    private Context context;
    private Form1007 current1007;
    private boolean flag;
    private boolean flag2;
    private Order currentOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_increase1007);
        context = this;

        //English default:
        Configuration config = new Configuration();
        Locale locale = new Locale("heb");

        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        Intent i = getIntent();
        currentForm1007 = (Form1007) i.getSerializableExtra("sampleObject");
        currentOrder=(Order)i.getSerializableExtra("currentOrder") ;

        signaturePad = (SignaturePad) findViewById(R.id.signaturePadIncrease);
        saveButton = (Button) findViewById(R.id.saveButtonIncrease);
        clearButton = (Button) findViewById(R.id.clearButtonIncrease);
        newCostET = findViewById(R.id.newCost);
        newDeatailsET = findViewById(R.id.newDeatails);
        updateBtn = findViewById(R.id.update);

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
                increaseUserSignature = signaturePad.getTransparentSignatureBitmap();
                isSign = true;
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
                isSign = false;
            }
        });


        database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference("users").child(Menu.user.getEmail().substring(0, Menu.user.getEmail().indexOf('@'))).child("opened1007");

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkFormFull() && isSign) {
                    updateSignature();
                    updateFirstCost();
                    updateBalanceProcess(Integer.parseInt(newCostET.getText().toString()));
                    updateDetails();
                    Toast.makeText(getApplicationContext(), "העדכון בוצע בהצלחה", Toast.LENGTH_LONG).show();
                    finish();

                }
            }
        });


    }

    public void updateFirstCost() {
        Query query = mDatabaseRef.orderByChild("equipmentId").equalTo(currentForm1007.getEquipmentId());
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ds.child("firstCost").getRef().setValue(newCostET.getText().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Erorr in updating data");
            }
        };
        query.addListenerForSingleValueEvent(eventListener);
    }

    public void updateDetails() {
        Query query = mDatabaseRef.orderByChild("equipmentId").equalTo(currentForm1007.getEquipmentId());
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String s = ds.child("details").getValue().toString();
                    ds.child("details").getRef().setValue(s + "," + newDeatailsET.getText().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Erorr in updating data");
            }
        };
        query.addListenerForSingleValueEvent(eventListener);
    }

    public void updateSignature() {
        Query query = mDatabaseRef.orderByChild("equipmentId").equalTo(currentForm1007.getEquipmentId());
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Form1007 form1007 = null;

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    form1007 = ds.getValue(Form1007.class);
                    break;
                }
                current1007 = form1007;
                current1007.setIncreaseUserId(Menu.user.getId());
                current1007.setIncreaseUserDarga(Menu.user.getDarga());
                current1007.setIncreaseUserName(Menu.user.getName());
                current1007.setIncreaseUserSignature(AdapterBitmap.adaptToString(increaseUserSignature));

                Date c = Calendar.getInstance().getTime();

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = df.format(c);
                current1007.setIncreaseDate(formattedDate);


                mDatabaseRef.child(String.valueOf(current1007.getId())).setValue(current1007);


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Erorr in updating data");
            }
        };
        query.addListenerForSingleValueEvent(eventListener);
    }

    public boolean checkFormFull() {
        boolean flag = true;
        if (newDeatailsET.getText().toString().equals("")) {
            flag = false;
            newDeatailsET.setError("נדרש למלא את השדה");

        }

        if (newCostET.getText().toString().equals("")) {
            flag = false;
            newCostET.setError("נדרש למלא את השדה");

        } else if(((Integer.parseInt(currentForm1007.getFirstCost())-Integer.parseInt(newCostET.getText().toString()))+Integer.parseInt(currentOrder.getBalanceProcess()))<=0){
            flag = false;
            newCostET.setError("אין מספיק תקציב בהזמנה");

        }


        return flag;

    }


    public void updateBalanceProcess(int newfirstCost) {

        DatabaseReference myRef = database.getReference("users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    for (DataSnapshot keyNode2 : keyNode.child("garages").getChildren()) {
                        for (DataSnapshot keyNode3 : keyNode2.child("orders").getChildren()) {
                            if (keyNode3.child("id").getValue().toString().equals(current1007.getOrderId())) {
                                int num1 = Integer.parseInt(current1007.getFirstCost()) - newfirstCost;
                                int tmp = Integer.valueOf(keyNode3.child("balance process").getValue().toString());
                                int tmp2 = tmp + num1;

                                keyNode3.child("balance process").getRef().setValue(String.valueOf(tmp2));
                                break;

                            }

                        }

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}



