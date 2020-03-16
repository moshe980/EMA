package com.example.ema.activitys;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ema.R;
import com.example.ema.classs.Form1007;
import com.example.ema.classs.Garage;
import com.example.ema.classs.Order;
import com.example.ema.classs.RepairType;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MakeForm extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CONTACT_PICKER_RESULT = 2;

    private FirebaseDatabase database;
    private StorageReference storageReference;
    private DatabaseReference myRef;
    private Spinner garageSpinner, orderSpinner, repairTypeSpinner, equipmentFamilySpinner;
    private EditText equipmentIdET, equipmentTypeET, speedometerET, detailsET, firstCostET;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextView enterDate, balanceTV, unitTV;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Garage currentGarage;
    private Order currentOrder;
    private RepairType currentRepairType;
    private List<Garage> garages;
    private DatabaseReference databaseUsers;
    private SignaturePad signaturePad;
    private Button saveButton, clearButton, choose1028, create1007Btn;
    private Context context;
    private Bitmap openUserSignature;
    private boolean isSign = false;
    private Uri mImageUri;
    private StorageTask mUploadTask;
    private Form1007 form1007;
    private ProgressBar progressBar;
    private String currentFamily;
    private boolean flag;
    private String contactNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_form);

        context = this;
        equipmentFamilySpinner = findViewById(R.id.equipmentFamily);
        progressBar = findViewById(R.id.progressBar2);
        currentGarage = new Garage();
        garages = new ArrayList<Garage>();
        garageSpinner = findViewById(R.id.garageName);
        orderSpinner = findViewById(R.id.orderNum);
        repairTypeSpinner = findViewById(R.id.repairType);
        radioGroup = findViewById(R.id.fuelAmount);
        enterDate = findViewById(R.id.enterDate);
        equipmentIdET = findViewById(R.id.equipmentNum);
        equipmentTypeET = findViewById(R.id.equipmentType);
        speedometerET = findViewById(R.id.speedometer);
        detailsET = findViewById(R.id.deatails);
        firstCostET = findViewById(R.id.firstCost);
        unitTV = findViewById(R.id.unit);
        create1007Btn = findViewById(R.id.create1007);
        balanceTV = findViewById(R.id.balanceForm);
        create1007Btn.setEnabled(true);
        signaturePad = (SignaturePad) findViewById(R.id.signaturePad);
        saveButton = (Button) findViewById(R.id.saveButton);
        clearButton = (Button) findViewById(R.id.clearButton);
        choose1028 = findViewById(R.id.choose1028);
        storageReference = FirebaseStorage.getInstance().getReference(Menu.user.getEmail().substring(0, Menu.user.getEmail().indexOf('@')))
                .child("uploads1028");

        //disable both buttons at start
        saveButton.setEnabled(false);
        clearButton.setEnabled(false);


        orderSpinner.setEnabled(false);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users").child(Menu.user.getEmail().substring(0, Menu.user.getEmail().indexOf('@'))).child("garages");

        //English default:
        Configuration config = new Configuration();
        Locale locale = new Locale("heb");

        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        readGarages(new FirebaseCallback() {

            @Override
            public void onCallback(final List<Garage> garages) {
                List<String> garageNames = new ArrayList<String>();
                for (Garage garage : garages) {
                    garageNames.add(garage.getName());
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MakeForm.this, android.R.layout.simple_list_item_1, garageNames);
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

                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(MakeForm.this, android.R.layout.simple_list_item_1, orderId);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        orderSpinner.setAdapter(adapter2);
                        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                for (Order order : currentGarage.getOrders()) {
                                    if (parent.getItemAtPosition(position).toString().substring(0, 10).equals(order.getId())) {
                                        int num = (Integer.valueOf(order.getAmounte()) - Integer.valueOf(order.getBalanceProcess()));
                                        DecimalFormat dtime = new DecimalFormat("#.##");
                                        double num2 = Double.parseDouble(dtime.format(num / Double.valueOf(order.getAmounte())));
                                        if (num2 <= 0.7) {
                                            currentOrder = order;
                                        } else if (num == 0) {
                                            enableFields(false);
                                        } else {
                                            currentOrder = order;
                                            balanceTV.setText("נוצלה יתרה של " + (int) (num2 * 100) + "%" + "\n"
                                                    + "נשארה יתרה של " + order.getBalance() + " שח");

                                        }


                                        Date d = convertStringToDate(order.getEndDate());
                                        if (Calendar.getInstance().getTime().after(d)) {
                                            enableFields(false);
                                            balanceTV.setText("תוקף ההזמנה נגמר! " + order.getEndDate());

                                        }
                                        if (Calendar.getInstance().getTime().getDay()==d.getDay()
                                        &&Calendar.getInstance().getTime().getMonth()==d.getMonth()
                                        &&Calendar.getInstance().getTime().getYear()==d.getYear()) {
                                            enableFields(true);
                                            currentOrder = order;
                                            balanceTV.setText("שים לב תוקף ההזמנה נגמר היום! " + order.getEndDate());

                                        }


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
        });
        equipmentFamilySpinner.setEnabled(true);
        List<String> equipmentFamilyList = new ArrayList<String>();
        equipmentFamilyList.add("גנרטורים");
        equipmentFamilyList.add("מלגזות");

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(MakeForm.this, android.R.layout.simple_list_item_1, equipmentFamilyList);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        equipmentFamilySpinner.setAdapter(adapter3);
        equipmentFamilySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentFamily = (String) parent.getItemAtPosition((position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        repairTypeSpinner.setEnabled(true);
        List<String> repairDeatails = new ArrayList<String>();
        final List<RepairType> repairTypes = new ArrayList<RepairType>();
        repairTypes.add(new RepairType("05", "טיפול תקופתי מורחב"));
        repairTypes.add(new RepairType("03", "טיפול תקופתי"));
        repairTypes.add(new RepairType("60", "טיפול שנתי"));
        repairTypes.add(new RepairType("73", "טיפול דו שנתי"));
        repairTypes.add(new RepairType("61", "חצי שנתי"));
        repairTypes.add(new RepairType("02", "תיקון מזדמן"));


        for (RepairType repairType : repairTypes) {
            repairDeatails.add(repairType.getRepairDeatail());
        }

        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(MakeForm.this, android.R.layout.simple_list_item_1, repairDeatails);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repairTypeSpinner.setAdapter(adapter4);
        repairTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (RepairType repairType : repairTypes) {
                    if (repairType.getRepairDeatail().equals(parent.getItemAtPosition((position)))) {
                        currentRepairType = repairType;

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        enterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MakeForm.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String pattern = "dd/MM/yyyy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

                Date currentTime = Calendar.getInstance().getTime();
                try {
                    Date chooseDate = dateFormat.parse(dayOfMonth + "/" + (month + 1) + "/" + year);

                    if (chooseDate.before(currentTime)) {
                        enterDate.setTextSize(20);
                        enterDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    } else
                        Toast.makeText(getApplicationContext(), "התאריך כניסה שגוי!", Toast.LENGTH_LONG).show();

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        };

        unitTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 2);


            }

        });


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
                openUserSignature = signaturePad.getTransparentSignatureBitmap();
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


        choose1028.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


        create1007Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);

                if (checkFormFull() && isSign) {


                    form1007 = new Form1007(currentFamily, Menu.user, openUserSignature, currentOrder.getId(), currentGarage.getName(), currentGarage.getId(), equipmentIdET.getText().toString()
                            , equipmentTypeET.getText().toString(), currentRepairType.getRepairCode(), speedometerET.getText().toString(),
                            radioButton.getText().toString(), enterDate.getText().toString(), detailsET.getText().toString(), firstCostET.getText().toString()
                            , unitTV.getText().toString());

                    int num1 = Integer.parseInt(currentOrder.getBalanceProcess());
                    int num2 = Integer.parseInt(form1007.getFirstCost());
                    currentOrder.setBalanceProcess(String.valueOf(num1 - num2));

                    if (mUploadTask != null && mUploadTask.isInProgress()) {
                        Toast.makeText(MakeForm.this, "נא להמתין", Toast.LENGTH_SHORT).show();
                    } else {
                        uploadFile();

                    }
                    databaseUsers = database.getReference("users").child(Menu.user.getEmail().substring(0, Menu.user.getEmail().indexOf('@'))).child("opened1007");
                    addForm1007(form1007);

                    updateBalanceProcess(Integer.parseInt(form1007.getFirstCost()));


                    create1007Btn.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    saveButton.setEnabled(false);
                    clearButton.setEnabled(false);
                    choose1028.setEnabled(false);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            form1007.makePdfForm(context);
                            finish();

                        }
                    }, 6000);


                }


            }
        });


    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference reference = storageReference.child("form1028" + form1007.getId() + 0 + "." + getFileExtension(mImageUri));
            mUploadTask = reference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                    firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String url = uri.toString();
                            myRef = database.getReference("users").child(Menu.user.getEmail().substring(0, Menu.user.getEmail().indexOf('@'))).child("opened1007");
                            Query query = myRef.orderByChild("equipmentId").equalTo(form1007.getEquipmentId());
                            ValueEventListener eventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    List<String> forms1028 = new ArrayList<>();
                                    forms1028.add(url);
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        ds.child("form1028").getRef().setValue(forms1028);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.out.println("Erorr in updating data");
                                }
                            };
                            query.addListenerForSingleValueEvent(eventListener);


                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MakeForm.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "לא בחרת טופס 1028", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser() {
        String[] mimeTypes = {"image/*", "application/pdf"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";

            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }

            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }


        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            choose1028.setText("הקובץ עלה");


        } else if (requestCode == CONTACT_PICKER_RESULT) {
            if (resultCode == Activity.RESULT_OK) {

                Uri contactData = data.getData();
                Cursor c = managedQuery(contactData, null, null, null, null);
                if (c.moveToFirst()) {


                    String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                    String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));


                    contactNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if (hasPhone.equalsIgnoreCase("1")) {
                        Cursor phones = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                null, null);
                        phones.moveToFirst();
                        contactNumber += "\n" + phones.getString(phones.getColumnIndex("data1"));
                        System.out.println("number is:" + contactNumber);
                        unitTV.setText(contactNumber);

                    }


                }
            }
        }
    }

    private void addForm1007(Form1007 form1007) {
        databaseUsers.child(String.valueOf(form1007.getId())).setValue(form1007);
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void enableFields(boolean flag) {
        equipmentIdET.setEnabled(flag);
        equipmentTypeET.setEnabled(flag);
        speedometerET.setEnabled(flag);
        detailsET.setEnabled(flag);
        firstCostET.setEnabled(flag);
        unitTV.setEnabled(flag);
        enterDate.setEnabled(flag);
        signaturePad.setEnabled(flag);
        create1007Btn.setEnabled(flag);
        saveButton.setEnabled(flag);
        clearButton.setEnabled(flag);
        choose1028.setEnabled(flag);
        equipmentFamilySpinner.setEnabled(flag);
        repairTypeSpinner.setEnabled(flag);


    }

    public boolean checkFormFull() {
        boolean flag = true;

        if (equipmentIdET.getText().toString().equals("")) {
            flag = false;
            equipmentIdET.setError("נדרש למלא את השדה");
        }
        if (equipmentTypeET.getText().toString().equals("")) {
            flag = false;
            equipmentTypeET.setError("נדרש למלא את השדה");

        }
        if (speedometerET.getText().toString().equals("")) {
            flag = false;
            speedometerET.setError("נדרש למלא את השדה");

        }
        if (detailsET.getText().toString().equals("")) {
            flag = false;
            detailsET.setError("נדרש למלא את השדה");

        }
        if (firstCostET.getText().toString().equals("")) {
            flag = false;
            firstCostET.setError("נדרש למלא את השדה");
        } else {
            int num1 = Integer.parseInt(currentOrder.getBalanceProcess());
            int num2 = Integer.parseInt(firstCostET.getText().toString());
            if ((num1 - num2 < 0)) {
                flag = false;
                firstCostET.setError("אין מספיק תקציב בהזמנה!");
            }
        }
        if (unitTV.getText().toString().equals("לחץ כאן להוספה")) {
            flag = false;
            unitTV.setError("נדרש למלא את השדה");

        }
        if (enterDate.getText().toString().equals("לחץ כאן לבחירת תאריך")) {
            flag = false;
            enterDate.setError("נדרש למלא את השדה");

        }
        return flag;
    }


    public void readGarages(final FirebaseCallback firebaseCallback) {
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
                            orders.add(order);
                    }
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

    private interface FirebaseCallback {
        void onCallback(List<Garage> list);
    }

    public void updateBalanceProcess(int firsCost) {
        if (flag == false) {
            DatabaseReference myRef = database.getReference("users");

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                        for (DataSnapshot keyNode2 : keyNode.child("garages").getChildren()) {
                            for (DataSnapshot keyNode3 : keyNode2.child("orders").getChildren()) {
                                if (keyNode3.child("id").getValue().toString().equals(form1007.getOrderId())) {
                                    int tmp = Integer.valueOf(currentOrder.getBalanceProcess());
                                    //     int tmp2 = tmp - firsCost;

                                    keyNode3.child("balance process").getRef().setValue(String.valueOf(tmp));

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
        flag = true;

    }

    public Date convertStringToDate(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date d = sdf.parse(s);
            return d;
        } catch (ParseException e) {
            Log.v("Exception", e.getLocalizedMessage());

        }

        return null;
    }

}

