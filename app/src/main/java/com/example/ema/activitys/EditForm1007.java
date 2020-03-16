package com.example.ema.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.ema.R;
import com.example.ema.classs.Form1007;
import com.example.ema.classs.Order;

import java.util.Locale;

public class EditForm1007 extends Activity {
    private Button closeForm1007Btn, increaseForm1007Btn, addFilesBtn, display1007Btn, confirm1007Btn;
    private TextView textView;
    private Context context;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_form1007);

        context = this;

        //English default:
        Configuration config = new Configuration();
        Locale locale = new Locale("heb");

        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int heigh = dm.heightPixels;


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setLayout((int) (width * 0.25), (int) (heigh * 0.7));
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().setLayout((int) (width * 0.5), (int) (heigh * 0.35));
        }
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);


        closeForm1007Btn = (Button) findViewById(R.id.close1007);
        increaseForm1007Btn = (Button) findViewById(R.id.increase1007);
        textView = (TextView) findViewById(R.id.equipmentId);
        addFilesBtn = findViewById(R.id.addFils);
        display1007Btn = findViewById(R.id.display1007);
        confirm1007Btn = findViewById(R.id.confirm1007);
        confirm1007Btn.setVisibility(View.GONE);

        Intent i = getIntent();
        final Form1007 currentForm1007 = (Form1007) i.getSerializableExtra("sampleObject");
        final Order currentOrder=(Order)i.getSerializableExtra("currentOrder") ;


        if (Menu.user.getJobTitle().equals("מנהל")){
            increaseForm1007Btn.setVisibility(View.GONE);
            closeForm1007Btn.setVisibility(View.GONE);
            addFilesBtn.setText("מסמכים מצורפים");
            confirm1007Btn.setVisibility(View.VISIBLE);
        }
        if(Menu.user.getJobTitle().equals("בוחן")&&currentForm1007.getStatus().equals("ממתין לאישור")){
            increaseForm1007Btn.setVisibility(View.GONE);
            closeForm1007Btn.setVisibility(View.GONE);
            addFilesBtn.setText("מסמכים מצורפים");
        }
        if(currentForm1007.getStatus().equals("אושר")){
            increaseForm1007Btn.setVisibility(View.GONE);
            closeForm1007Btn.setVisibility(View.GONE);
            addFilesBtn.setText("מסמכים מצורפים");
        }





        if (i.getSerializableExtra("flag") != null) {
            flag = (boolean) i.getSerializableExtra("flag");
        }
        closeForm1007Btn.setEnabled(false);

        if (flag) {
            closeForm1007Btn.setBackgroundColor(Color.parseColor("#3379FF"));
            closeForm1007Btn.setEnabled(true);

        }
        textView.setText("צ': " + currentForm1007.getEquipmentId());

        increaseForm1007Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Increase1007.class);
                intent.putExtra("sampleObject", currentForm1007);
                intent.putExtra("currentOrder", currentOrder);

                startActivity(intent);
                finish();
            }
        });
        addFilesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddFiles.class);
                intent.putExtra("sampleObject", currentForm1007);
                startActivity(intent);
                finish();

            }
        });

        closeForm1007Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Close1007.class);
                intent.putExtra("sampleObject", currentForm1007);
                intent.putExtra("currentOrder", currentOrder);
                startActivity(intent);
                finish();

            }
        });
        display1007Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentForm1007.makePdfForm(context);
                finish();

            }
        });
        confirm1007Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ManagerSign.class);
                intent.putExtra("sampleObject", currentForm1007);
                intent.putExtra("currentOrder", currentOrder);
                startActivity(intent);
                finish();


            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
