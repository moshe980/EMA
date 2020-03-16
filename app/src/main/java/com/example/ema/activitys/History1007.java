package com.example.ema.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ema.R;
import com.example.ema.activitys.EditForm1007;
import com.example.ema.classs.Form1007;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class History1007 extends AppCompatActivity {
    private Button searchBtn;
    private EditText equipmendIdET;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private List<Form1007> history1007List;
    private Context context;
    private TableLayout tableLayout;
    private int counter;
    private TableRow tableRow, titleRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history1007);

        context = this;

        //English default:
        Configuration config = new Configuration();
        Locale locale = new Locale("heb");

        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        equipmendIdET = findViewById(R.id.equipmentIdSearch);
        searchBtn = findViewById(R.id.search);
        tableLayout = (TableLayout) findViewById(R.id.historyTable);
        titleRow = findViewById(R.id.titleRowHistory1007);
        tableLayout.setStretchAllColumns(true);
        history1007List = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Query query = myRef.orderByChild("equipmentId").equalTo(equipmendIdET.getText().toString());

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        history1007List.clear();
                        counter = 1;
                        tableLayout.removeAllViewsInLayout();
                        tableLayout.addView(titleRow);

                        for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                            for (DataSnapshot keyNode2 : keyNode.child("closed1007").getChildren()) {
                                Form1007 form1007 = keyNode2.getValue(Form1007.class);
                                if (form1007 != null && form1007.getEquipmentId().equals(equipmendIdET.getText().toString())) {
                                    history1007List.add(form1007);

                                }

                            }


                        }
                        if (history1007List.size() != 0) {

                            for (final Form1007 form1007 : history1007List) {
                                tableRow = new TableRow(context);
                                //Pos
                                final TextView c1 = new TextView(context);
                                c1.setText(String.valueOf(counter));
                                c1.setGravity(Gravity.CENTER);
                                tableRow.addView(c1);
                                //Equipment Id
                                TextView c2 = new TextView(context);
                                c2.setText(form1007.getEquipmentId());
                                c2.setGravity(Gravity.CENTER);
                                tableRow.addView(c2);
                                //Close Date
                                TextView c3 = new TextView(context);
                                c3.setText(form1007.getCloseDate());
                                c3.setGravity(Gravity.CENTER);
                                tableRow.addView(c3);
                                //Equipment Type
                                TextView c4 = new TextView(context);
                                c4.setText(form1007.getEquipmentType());
                                c4.setGravity(Gravity.CENTER);
                                tableRow.addView(c4);
                                //Unit
                                TextView c5 = new TextView(context);
                                c5.setText(form1007.getUnit());
                                c5.setGravity(Gravity.CENTER);
                                tableRow.addView(c5);
                                //Repair Type
                                TextView c6 = new TextView(context);
                                c6.setText(form1007.getRepairType());
                                c6.setGravity(Gravity.CENTER);
                                tableRow.addView(c6);


                                tableRow.setClickable(true);
                                tableRow.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context, EditForm1007.class);
                                        intent.putExtra("sampleObject", form1007);

                                        startActivity(intent);

                                    }
                                });
                                tableLayout.addView(tableRow);
                                counter++;


                            }


                        } else {
                            Toast.makeText(getApplicationContext(), "אין תוצאות חיפוש", Toast.LENGTH_LONG).show();

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }


}
