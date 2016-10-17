package com.example.jhon.smiserviciostablet;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jhon.smiserviciostablet.Adapters.ListDriverPetitionsAdapter;
import com.example.jhon.smiserviciostablet.Adapters.ListHomePetitionsAdapter;
import com.example.jhon.smiserviciostablet.Interfaces.QueryInterface;
import com.example.jhon.smiserviciostablet.Models.DriverUserPetition;
import com.example.jhon.smiserviciostablet.Models.Homepetitions;
import com.example.jhon.smiserviciostablet.Models.Users;
import com.example.jhon.smiserviciostablet.Models.driverpetitions;
import com.example.jhon.smiserviciostablet.Net.DriverPetitionsDao;
import com.example.jhon.smiserviciostablet.Net.HomePetitionsDao;
import com.example.jhon.smiserviciostablet.Net.UsersDao;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;



public class DriverServicesActivity extends AppCompatActivity implements QueryInterface, DriverPetitionsDao.QueryInterfaceDriverPetitions, DriverPetitionsDao.UpdateDriverPetitionsInterface, UsersDao.UsersDaoUpdateInterface, ListDriverPetitionsAdapter.HomePetitionsInterface {
    Toolbar toobar;
    CollapsingToolbarLayout collapse;
    ListView listView;
    MobileServiceClient mClient;
    MobileServiceTable<driverpetitions> mTableUsers;
    MobileServiceList<driverpetitions> mList;
    DriverPetitionsDao driverPetitionsDao;
    UsersDao usersDao;
    List<Users> dataUsers;
    List<driverpetitions> data;
    List<DriverUserPetition> dataAdapter;
    ListDriverPetitionsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_services);
        setContentView(R.layout.activity_home_petitions);
        data = new ArrayList<>();
        dataUsers = new ArrayList<>();
        dataAdapter = new ArrayList<>();

        try {
            mClient = new MobileServiceClient("https://smiserviciosmovil.azure-mobile.net/",
                    "qIufyUhXNGYkLUXenUUDufQFPMdcUm65",
                    this);

            mTableUsers = mClient.getTable(driverpetitions.class);
            driverPetitionsDao = new DriverPetitionsDao(mClient,this,this);
            usersDao = new UsersDao(mClient,this,this);
            driverPetitionsDao.getAllDriverPetitions();

        } catch (MalformedURLException e) {
            Toast.makeText(this,"No fue posible conectar con la base de datos",Toast.LENGTH_SHORT).show();
        }

        toobar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.list_view);
        setSupportActionBar(toobar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Peticiones de Conductor");
        collapse = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapse.setTitle("Administrador");
        collapse.setExpandedTitleColor(getResources().getColor(android.R.color.white));

    }

    @Override
    public void OnQueryFinish(int state, MobileServiceList<Users> list) {
        dataUsers.add(list.get(0));
        if (dataUsers.size() == data.size()){
            for (int i = 0; i < data.size(); i++) {
                DriverUserPetition driverUser = new DriverUserPetition();
                driverUser.setDriverpetitions1(data.get(i));
                driverUser.setUsers(dataUsers.get(i));
                dataAdapter.add(driverUser);
            }
            adapter = new ListDriverPetitionsAdapter(dataAdapter,this,null,this);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void OnQueryFinishDriver(int state, MobileServiceList<driverpetitions> list) {
        data.addAll(list);
        for (driverpetitions driverpetitions : data) {
            usersDao.getUserById(driverpetitions.getUserid());
        }
    }       

    @Override
    public void OnUpdateFinishDriver(int state, String e, driverpetitions homepetitions, int i) {
        if (state == HomePetitionsDao.INSERT_CORRECT){
            dataAdapter.remove(i);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void OnUserUpdate(int type, int state, String e, Users users) {
        
    }

    @Override
    public void OnButtonClickListener(int type, DriverUserPetition data, int pos) {
        switch (type){
            case ListHomePetitionsAdapter.ACEPTAR:
                data.getDriverpetitions1().setState(1);
                driverPetitionsDao.updatePetition(data.getDriverpetitions1(),pos);
                break;
            case ListHomePetitionsAdapter.RECHAZAR:
                data.getDriverpetitions1().setState(1);
                driverPetitionsDao.updatePetition(data.getDriverpetitions1(),pos);
                break;

        }

    }
}

