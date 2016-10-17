package com.example.jhon.smiserviciostablet;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout home, driver, roadassitence, users;
    Toolbar toobar;
    CollapsingToolbarLayout collapse;
    ImageView icon1;

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
                startActivity(new Intent(this,DriverServicesActivity.class));
                break;
            case R.id.client_services_home:
                startActivity(new Intent(this,HomePetitionsActivity.class));
                break;
            case R.id.client_services_roadasistence:
                startActivity(new Intent(this,RoadPetitionsAcitivity.class));
                break;
            case R.id.client_services_users:
                startActivity(new Intent(this,UsersListActivity.class));
                break;

        }
    }

}