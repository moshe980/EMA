package com.example.ema.activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.ema.classs.Bill;
import com.example.ema.fregments.Enter1028;
import com.example.ema.fregments.EnterSign1007;
import com.example.ema.fregments.Estimate;
import com.example.ema.classs.Form1007;
import com.example.ema.R;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;

import com.example.ema.ui.main.SectionsPagerAdapter;

public class AddFiles extends AppCompatActivity implements EnterSign1007.OnFragmentInteractionListener, Estimate.OnFragmentInteractionListener, Enter1028.OnFragmentInteractionListener,
        Bill.OnFragmentInteractionListener {
    private TextView title;
    public static Form1007 currentForm1007;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_files);

        Intent i = getIntent();
        currentForm1007 = (Form1007) i.getSerializableExtra("sampleObject");

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        title=findViewById(R.id.title);
        title.setText("צ': "+currentForm1007.getEquipmentId());

        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}