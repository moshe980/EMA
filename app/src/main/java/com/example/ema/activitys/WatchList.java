package com.example.ema.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.ema.R;
import com.example.ema.classs.Form1007;
import com.example.ema.classs.Garage;
import com.example.ema.classs.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WatchList extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private List<Form1007> open1007List;
    private Context context;
    private TableLayout tableLayout;
    private int counter;
    private boolean flag = false;
    private TableRow tableRow;
    private Spinner garageSpinner, orderSpinner;
    private Button search;
    private List<Garage> garages;
    private Garage currentGarage;
    private Order currentOrder;
    private TableRow titleRow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);
        context = this;

        //English default:
        Configuration config = new Configuration();
        Locale locale = new Locale("heb");

        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        garageSpinner = findViewById(R.id.garageNameWatchList);
        garages = new ArrayList<Garage>();
        orderSpinner = findViewById(R.id.orderNumWatchList);
        search = findViewById(R.id.searchWatchList);
        tableLayout = (TableLayout) findViewById(R.id.table);
        titleRow=findViewById(R.id.firstRowWatchList);
        tableLayout.setStretchAllColumns(true);
        open1007List = new ArrayList<>();
        database = FirebaseDatabase.getInstance();

        readGarages(new FirebaseCallback() {

            @Override
            public void onCallback(final List<Garage> garages) {
                List<String> garageNames = new ArrayList<String>();
                for (Garage garage : garages) {
                    garageNames.add(garage.getName());
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(WatchList.this, android.R.layout.simple_list_item_1, garageNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                garageSpinner.setAdapter(adapter);
                garageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        for (Garage garage : garages) {
                            if (parent.getItemAtPosition((position)).equals(garage.getName())) {
                                currentGarage = garage;
                                orderSpinner.setEnabled(true);
                                break;
                            }
                        }
                        orderSpinner.setEnabled(true);
                        List<String> orderId = new ArrayList<String>();
                        for (int i = 0; i < currentGarage.getOrders().size(); i++) {
                            orderId.add(currentGarage.getOrders().get(i).getId() + "-" + currentGarage.getOrders().get(i).getDeatails());
                        }

                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(WatchList.this, android.R.layout.simple_list_item_1, orderId);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        orderSpinner.setAdapter(adapter2);
                        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                for (Order order : currentGarage.getOrders()) {
                                    if (parent.getItemAtPosition(position).toString().substring(0, 10).equals(order.getId())) {
                                        currentOrder = order;
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }

            ;
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                readOpen1007();
            }
        });


    }


    public void readOpen1007() {
        myRef = database.getReference("users").child(Menu.user.getEmail().substring(0, Menu.user.getEmail().indexOf('@'))).child("opened1007");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                open1007List.clear();
                counter = 1;
                tableLayout.removeAllViewsInLayout();
                tableLayout.addView(titleRow);

                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Form1007 form1007 = keyNode.getValue(Form1007.class);
                    if (form1007.getOrderId().equals(currentOrder.getId()))
                        open1007List.add(form1007);
                }
                for (final Form1007 form1007 : open1007List) {
                    tableRow = new TableRow(context);
                    //Pos
                    final TextView c1 = new TextView(context);
                    c1.setTextSize(10);
                    c1.setText(String.valueOf(counter));
                    c1.setGravity(Gravity.CENTER);
                    tableRow.addView(c1);
                    //Equipment Id
                    TextView c2 = new TextView(context);
                    c2.setTextSize(10);
                    c2.setText(form1007.getEquipmentId());
                    c2.setGravity(Gravity.CENTER);
                    tableRow.addView(c2);
                    //Enter Date
                    TextView c3 = new TextView(context);
                    c3.setTextSize(10);
                    c3.setText(form1007.getEnterDate());
                    c3.setGravity(Gravity.CENTER);
                    tableRow.addView(c3);
                    //Equipment Type
                    TextView c4 = new TextView(context);
                    c4.setTextSize(10);
                    c4.setText(form1007.getEquipmentType());
                    c4.setGravity(Gravity.CENTER);
                    tableRow.addView(c4);
                    //Unit
                    TextView c5 = new TextView(context);
                    c5.setTextSize(10);
                    c5.setText(form1007.getUnit());
                    c5.setGravity(Gravity.CENTER);
                    tableRow.addView(c5);
                    //Repair Type
                    TextView c6 = new TextView(context);
                    c6.setTextSize(10);
                    c6.setText(form1007.getRepairType());
                    c6.setGravity(Gravity.CENTER);
                    tableRow.addView(c6);
                    //First Cost
                    TextView c7 = new TextView(context);
                    c7.setTextSize(10);
                    c7.setText(form1007.getFirstCost());
                    c7.setGravity(Gravity.CENTER);
                    tableRow.addView(c7);
                    //Order Id
                    TextView c8 = new TextView(context);
                    c8.setTextSize(10);
                    c8.setText(form1007.getOrderId());
                    c8.setGravity(Gravity.CENTER);
                    tableRow.addView(c8);
                    //has1028
                    final TextView c9 = new TextView(context);
                    c9.setTextSize(10);

                    if (form1007.getForm1028() == null) {
                        c9.setText("לא");
                    } else {
                        c9.setText("כן");
                    }
                    c9.setGravity(Gravity.CENTER);
                    tableRow.addView(c9);
                    //isGarageSigned
                    final TextView c10 = new TextView(context);
                    c10.setTextSize(10);
                    if (form1007.getForms1007Signed() == null) {
                        c10.setText("לא");
                    } else {
                        c10.setText("כן");
                    }
                    c10.setGravity(Gravity.CENTER);
                    tableRow.addView(c10);
                    //hasEstimate
                    final TextView c11 = new TextView(context);
                    c11.setTextSize(10);
                    if (form1007.getEstimats() == null) {
                        c11.setText("לא");
                    } else {
                        c11.setText("כן");
                    }
                    c11.setGravity(Gravity.CENTER);
                    tableRow.addView(c11);
                    //hasEstimate
                    final TextView c12 = new TextView(context);
                    c12.setTextSize(10);
                    if (form1007.getStatus() == null) {
                        c12.setText(" ");
                    } else {
                        c12.setText(form1007.getStatus());
                    }
                    c12.setGravity(Gravity.CENTER);
                    tableRow.addView(c12);


                    tableRow.setClickable(true);
                    tableRow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, EditForm1007.class);
                            intent.putExtra("sampleObject", form1007);
                            intent.putExtra("currentOrder",  currentOrder);

                            if (c11.getText().toString().equals("כן")
                                    && c10.getText().toString().equals("כן")
                                    && c9.getText().toString().equals("כן")) {
                                flag = true;
                            }
                            intent.putExtra("sampleObject", form1007);
                            intent.putExtra("flag", flag);
                            intent.putExtra("currentOrder",  currentOrder);

                            startActivity(intent);


                        }
                    });
                    tableLayout.addView(tableRow);
                    counter++;

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Error in reading");
            }
        });

    }

    public void readGarages(final FirebaseCallback firebaseCallback) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users").child(Menu.user.getEmail().substring(0, Menu.user.getEmail().indexOf('@'))).child("garages");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {

                    String id = keyNode.child("id").getValue().toString();
                    String name = keyNode.child("name").getValue().toString();

                    List<Order> orders = new ArrayList<Order>();
                    for (int i = 0; i < keyNode.child("orders").getChildrenCount(); i++) {
                        String orderId = keyNode.child("orders").child(String.valueOf(i)).child("id").getValue().toString();
                        String start = keyNode.child("orders").child(String.valueOf(i)).child("start date").getValue().toString();
                        String end = keyNode.child("orders").child(String.valueOf(i)).child("end date").getValue().toString();
                        String amount = keyNode.child("orders").child(String.valueOf(i)).child("amount").getValue().toString();
                        String deatails = keyNode.child("orders").child(String.valueOf(i)).child("details").getValue().toString();
                        String balance = keyNode.child("orders").child(String.valueOf(i)).child("balance").getValue().toString();
                        String balanceProcess = keyNode.child("orders").child(String.valueOf(i)).child("balance process").getValue().toString();

                        Order order = new Order(orderId, start, end, amount, deatails, balance, balanceProcess);
                        if (!order.getBalance().equals("0"))
                            orders.add(order);                    }
                    garages.add(new Garage(id, name, orders));
                }
                firebaseCallback.onCallback(garages);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Error in reading");
            }
        });
    }

    interface FirebaseCallback {
        void onCallback(List<Garage> list);
    }


}
