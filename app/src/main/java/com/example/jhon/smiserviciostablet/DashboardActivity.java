package com.example.jhon.smiserviciostablet;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.jhon.smiserviciostablet.Util.Constants;


public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout home, driver, roadassitence, users,homeTaken,roadAssitanceTaken, driverTaken,client_services_carborrow, client_services_carborrow_taken,client_all_services;
    Toolbar toobar;
    CollapsingToolbarLayout collapse;
    ImageView icon1;
    public static final int NORMAL_INTENT = 0;
    public static final int TAKEN_INTENT = 1;
    public static final String KIND_PETITION = "kindPetition";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toobar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toobar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapse = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapse.setTitle("Administrador");
        collapse.setExpandedTitleColor(getResources().getColor(android.R.color.black));
        collapse.setCollapsedTitleTextColor(getResources().getColor(android.R.color.black));


        home = (LinearLayout) findViewById(R.id.client_services_home);
        driver = (LinearLayout) findViewById(R.id.client_services_driver);
        roadassitence = (LinearLayout) findViewById(R.id.client_services_roadasistence);
        homeTaken = (LinearLayout) findViewById(R.id.client_services_home_taken);
        homeTaken.setOnClickListener(this);
        driverTaken  = (LinearLayout) findViewById(R.id.client_services_driver_taken);
        driverTaken.setOnClickListener(this);
        roadAssitanceTaken = (LinearLayout) findViewById(R.id.client_services_roadasistence_taken);
        roadAssitanceTaken.setOnClickListener(this);
        client_services_carborrow = (LinearLayout) findViewById(R.id.client_services_carborrow);
        client_services_carborrow.setOnClickListener(this);

        client_services_carborrow_taken = (LinearLayout) findViewById(R.id.client_services_carborrow_taken);
        client_services_carborrow_taken.setOnClickListener(this);

        client_all_services = (LinearLayout) findViewById(R.id.client_all_services_users);
        client_all_services.setOnClickListener(this);



        icon1 = (ImageView) findViewById(R.id.icon1_client_services);
        users = (LinearLayout) findViewById(R.id.client_services_users);

        home.setOnClickListener(this);
        driver.setOnClickListener(this);
        roadassitence.setOnClickListener(this);
        users.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.client_services_driver:
                startActivity(new Intent(this,DriverServicesActivity.class).putExtra(KIND_PETITION,NORMAL_INTENT));
                break;
            case R.id.client_services_driver_taken:
                startActivity(new Intent(this,DriverServicesActivity.class).putExtra(KIND_PETITION,TAKEN_INTENT));
                break;
            case R.id.client_services_home:
                startActivity(new Intent(this,HomePetitionsActivity.class).putExtra(KIND_PETITION,NORMAL_INTENT));
                break;
            case R.id.client_services_home_taken:
                startActivity(new Intent(this,HomePetitionsActivity.class).putExtra(KIND_PETITION,TAKEN_INTENT));
                break;
            case R.id.client_services_roadasistence:
                startActivity(new Intent(this,RoadPetitionsAcitivity.class).putExtra(KIND_PETITION,NORMAL_INTENT));
                break;
            case R.id.client_services_roadasistence_taken:
                startActivity(new Intent(this,RoadPetitionsAcitivity.class).putExtra(KIND_PETITION,TAKEN_INTENT));
                break;
            case R.id.client_services_users:
                startActivity(new Intent(this,UsersListActivity.class).putExtra(Constants.IS_ALL_USERS,true));
                break;
            case R.id.client_services_carborrow:
                startActivity(new Intent(this,CarBorrowActivity.class).putExtra(KIND_PETITION,NORMAL_INTENT));
                break;
            case R.id.client_services_carborrow_taken:
                startActivity(new Intent(this,CarBorrowActivity.class).putExtra(KIND_PETITION,TAKEN_INTENT));
                break;
            case R.id.client_all_services_users:
                startActivity(new Intent(this,UsersListActivity.class).putExtra(Constants.IS_ALL_USERS,false));
                break;
        }
    }

}