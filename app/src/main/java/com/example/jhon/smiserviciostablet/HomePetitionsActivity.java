package com.example.jhon.smiserviciostablet;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jhon.smiserviciostablet.Adapters.ListHomePetitionsAdapter;
import com.example.jhon.smiserviciostablet.Adapters.ListUserAdapter;
import com.example.jhon.smiserviciostablet.Interfaces.QueryInterface;
import com.example.jhon.smiserviciostablet.Models.Homepetitions;
import com.example.jhon.smiserviciostablet.Models.Users;
import com.example.jhon.smiserviciostablet.Net.HomePetitionsDao;
import com.example.jhon.smiserviciostablet.Net.UsersDao;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class HomePetitionsActivity extends AppCompatActivity implements HomePetitionsDao.QueryInterfaceHomePetitions, QueryInterface, ListHomePetitionsAdapter.HomePetitionsInterface, UsersDao.UsersDaoUpdateInterface, HomePetitionsDao.UpdateHomePetitionsInterface {
    Toolbar toobar;
    CollapsingToolbarLayout collapse;
    ListView listView;
    MobileServiceClient mClient;
    MobileServiceTable<Homepetitions> mTableUsers;
    MobileServiceList<Homepetitions> mList;
    HomePetitionsDao homePetitionsDao;
    UsersDao usersDao;
    List<Users> dataUsers;
    List<Homepetitions> data;
    ListHomePetitionsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_petitions);
        data = new ArrayList<>();
        dataUsers = new ArrayList<>();

        try {
            mClient = new MobileServiceClient("https://smiserviciosmovil.azure-mobile.net/",
                    "qIufyUhXNGYkLUXenUUDufQFPMdcUm65",
                    this);

            mTableUsers = mClient.getTable(Homepetitions.class);
            homePetitionsDao = new HomePetitionsDao(mClient,this,this);
            usersDao = new UsersDao(mClient,this,this);
            homePetitionsDao.getAllHomePetitions();

        } catch (MalformedURLException e) {
            Toast.makeText(this,"No fue posible conectar con la base de datos",Toast.LENGTH_SHORT).show();
        }

        toobar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.list_view);
        setSupportActionBar(toobar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Peticiones de hogar");
        collapse = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapse.setTitle("Administrador");
        collapse.setExpandedTitleColor(getResources().getColor(android.R.color.white));
        collapse.setCollapsedTitleTextColor(getResources().getColor(android.R.color.black));

    }







    @Override
    public void OnQueryFinishHome(int state, MobileServiceList<Homepetitions> list) {
        data.addAll(list);
        for (Homepetitions homepetitions : data) {
            usersDao.getUserById(homepetitions.getUserid());
        }
    }

    @Override
    public void OnQueryFinish(int state, MobileServiceList<Users> list) {
        dataUsers.add(list.get(0));
        if (dataUsers.size() == data.size()){
            adapter = new ListHomePetitionsAdapter(data,this,dataUsers,this);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void OnButtonClickListener(int type, Homepetitions homepetitions, int i) {
        switch (type){
            case ListHomePetitionsAdapter.ACEPTAR:
                homepetitions.setState(1);
                homePetitionsDao.updatePetition(homepetitions);
                break;
            case ListHomePetitionsAdapter.RECHAZAR:
                homepetitions.setState(2);
                homePetitionsDao.updatePetition(homepetitions);
                break;

        }
    }

    @Override
    public void OnUserUpdate(int type, int state, String e,Users users) {
    }

    @Override
    public void OnUpdateFinishHome(int state, String e,Homepetitions homepetitions, int i) {
        if (state == HomePetitionsDao.INSERT_CORRECT){
            data.remove(homepetitions);
            for (Homepetitions homepetitionsdata : data) {
                usersDao.getUserById(homepetitionsdata.getUserid());
            }
        }
    }
}