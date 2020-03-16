package com.example.ema.activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ema.classs.Form1007;
import com.example.ema.R;
import com.example.ema.classs.AdapterBitmap;
import com.example.ema.classs.Order;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Close1007 extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView exitDate;
    private EditText endCostET, billIdET;
    private Button saveBtn, chooseBillBtn;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Context context;
    private DatabaseReference mDatabaseRef;
    private FirebaseDatabase database;
    private Form1007 current1007;
    private SignaturePad signaturePad;
    private Button saveButton, clearButton;
    private Bitmap closeUserSignature;
    private boolean isSign = false;
    private Uri mImageUri;
    private StorageTask mUploadTask;
    private StorageReference storageReference;
    private DatabaseReference myRef;
    private ProgressBar progressBar;
    private Form1007 currentForm1007;
    private boolean flag2;
    private Order currentOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close1007);

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

        database = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference(Menu.user.getEmail().substring(0, Menu.user.getEmail().indexOf('@')))
                .child("uploadBiil");
        exitDate = findViewById(R.id.exitDate);
        progressBar = findViewById(R.id.progressBar5);
        endCostET = findViewById(R.id.endCost);
        saveBtn = findViewById(R.id.save);
        billIdET = findViewById(R.id.billId);
        signaturePad = (SignaturePad) findViewById(R.id.signaturePad);
        saveButton = (Button) findViewById(R.id.saveButton);
        clearButton = (Button) findViewById(R.id.clearButton);
        chooseBillBtn = findViewById(R.id.uploadBill);
        chooseBillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });

        //disable both buttons at start
        saveButton.setEnabled(false);
        clearButton.setEnabled(false);


        exitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        context, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
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
                        exitDate.setTextSize(20);
                        exitDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    } else
                        Toast.makeText(getApplicationContext(), "התאריך יציאה שגוי!", Toast.LENGTH_LONG).show();

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        };

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
                closeUserSignature = signaturePad.getTransparentSignatureBitmap();
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

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFormFull() && isSign) {
                    updateBalanceProcess(Integer.parseInt(endCostET.getText().toString()));
                    mDatabaseRef = database.getReference("users").child(Menu.user.getEmail().substring(0, Menu.user.getEmail().indexOf('@'))).child("opened1007");

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
                            current1007.setBillId(billIdET.getText().toString());
                            current1007.setExitDate(exitDate.getText().toString());
                            current1007.setEndCost(endCostET.getText().toString());
                            current1007.setCloseUserId(Menu.user.getId());
                            current1007.setCloseUserDarga(Menu.user.getDarga());
                            current1007.setCloseUserName(Menu.user.getName());
                            current1007.setCloseUserSignature(AdapterBitmap.adaptToString(closeUserSignature));
                            current1007.setStatus("ממתין לאישור");

                            if (current1007.getIncreaseUserSignature()==null){
                                current1007.setIncreaseUserName(current1007.getCurrentUserName());
                                current1007.setIncreaseDate(current1007.getOpenDate());
                                current1007.setIncreaseUserDarga(current1007.getCurrentUserDarga());
                                current1007.setIncreaseUserId(current1007.getCurrentUserId());
                                current1007.setIncreaseUserSignature(current1007.getOpenUserSignature());
                            }

                            Date c = Calendar.getInstance().getTime();

                            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            String formattedDate = df.format(c);
                            current1007.setCloseDate(formattedDate);


                            mDatabaseRef.child(String.valueOf(current1007.getId())).setValue(current1007);

                            //     for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            //      ds.getRef().removeValue();
                            //  }


                            if (mUploadTask != null && mUploadTask.isInProgress()) {
                                Toast.makeText(Close1007.this, "נא להמתין", Toast.LENGTH_SHORT).show();
                            } else {
                                uploadFile();


                            }
                            saveBtn.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            saveButton.setEnabled(false);
                            clearButton.setEnabled(false);
                            chooseBillBtn.setEnabled(false);

                            Toast.makeText(getApplicationContext(), "העדכון בוצע בהצלחה", Toast.LENGTH_LONG).show();
                            finish();
                            current1007.makePdfForm(context);


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };
                    query.addListenerForSingleValueEvent(eventListener);


                }
            }
        });


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


        //Intent intent = new Intent();
        // intent.setType("image/*");
        // intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            chooseBillBtn.setText("הקובץ עלה");
        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public boolean checkFormFull() {
        boolean flag = true;

        if (exitDate.getText().toString().equals("לחץ כאן לבחירת תאריך")) {
            flag = false;
            exitDate.setError("נדרש למלא את השדה");
        }
        if (endCostET.getText().toString().equals("")) {
            flag = false;
            endCostET.setError("נדרש למלא את השדה");

        } else if(((Integer.parseInt(currentForm1007.getFirstCost())-Integer.parseInt(endCostET.getText().toString()))+Integer.parseInt(currentOrder.getBalanceProcess()))<=0){
            flag = false;
            endCostET.setError("אין מספיק תקציב בהזמנה");

        }
        if (billIdET.getText().toString().equals("")) {
            flag = false;
            billIdET.setError("נדרש למלא את השדה");
        }
        if (chooseBillBtn.getText().toString().equals("הכנס חשבונית")) {
            flag = false;
            chooseBillBtn.setError("נדרש להוסיף חשבונית");
        }


        return flag;
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference reference = storageReference.child("bill" + currentForm1007.getId() + 0 + "." + getFileExtension(mImageUri));
            mUploadTask = reference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                    firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String url = uri.toString();
                            myRef = database.getReference("users").child(Menu.user.getEmail().substring(0, Menu.user.getEmail().indexOf('@'))).child("opened1007");
                            Query query = myRef.orderByChild("equipmentId").equalTo(currentForm1007.getEquipmentId());
                            ValueEventListener eventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    List<String> bills = new ArrayList<>();
                                    bills.add(url);
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        ds.child("bill").getRef().setValue(bills);
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
                    Toast.makeText(Close1007.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "לא בחרת חשבונית", Toast.LENGTH_SHORT).show();
        }

    }
    public void updateBalanceProcess(int newfirstCost) {
        DatabaseReference myRef2 = database.getReference("users");
        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    for (DataSnapshot keyNode2 : keyNode.child("garages").getChildren()) {
                        for (DataSnapshot keyNode3 : keyNode2.child("orders").getChildren()) {
                            if (keyNode3.child("id").getValue().toString().equals(current1007.getOrderId())) {
                                int num1 = Integer.parseInt(current1007.getFirstCost()) - newfirstCost;
                                int tmp = Integer.valueOf(keyNode3.child("balance process").getValue().toString());
                                int tmp2 = tmp + num1;

                                System.out.println("here");
                                keyNode3.child("balance process").getRef().setValue(String.valueOf(tmp2));
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
