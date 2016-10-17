package com.example.jhon.smiserviciostablet;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jhon.smiserviciostablet.Adapters.ListUserAdapter;
import com.example.jhon.smiserviciostablet.Interfaces.QueryInterface;
import com.example.jhon.smiserviciostablet.Models.Users;
import com.example.jhon.smiserviciostablet.Net.UsersDao;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


public class UsersListActivity extends AppCompatActivity implements QueryInterface, ListUserAdapter.OnButtonClickListener, UsersDao.UsersDaoUpdateInterface {
    Toolbar toobar;
    CollapsingToolbarLayout collapse;
    ListView listView;
    ListUserAdapter adapter;
    List<Users> data;
    MobileServiceClient mClient;
    MobileServiceTable<Users> mTableUsers;
    MobileServiceList<Users> mList;
    UsersDao usersDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        try {
            mClient = new MobileServiceClient("https://smiserviciosmovil.azure-mobile.net/",
                    "qIufyUhXNGYkLUXenUUDufQFPMdcUm65",
                    this);

            mTableUsers = mClient.getTable(Users.class);
            usersDao = new UsersDao(mClient,this,this);
            usersDao.getAllUsers();

        } catch (MalformedURLException e) {
            Toast.makeText(this,"No fue posible conectar con la base de datos",Toast.LENGTH_SHORT).show();
        }

        toobar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.list_view);
        setSupportActionBar(toobar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapse = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapse.setTitle("Administrador");
        collapse.setExpandedTitleColor(getResources().getColor(android.R.color.white));
        collapse.setCollapsedTitleTextColor(getResources().getColor(android.R.color.black));

    }

    @Override
    public void OnQueryFinish(int state, MobileServiceList<Users> list) {
    if (state == UsersDao.INSERT_CORRECT){
        data = new ArrayList<>();
        data.addAll(list);
        adapter = new ListUserAdapter(data,this,this);
        listView.setAdapter(adapter);
    }
    }

    @Override
    public void OnButtonclick(int type, Users users) {
        switch (type){
            case ListUserAdapter.ACEPTAR:
                users.setIsvalid(1);
                usersDao.updateUser(users);
                break;

            case ListUserAdapter.RECHAZAR:
                users.setIsvalid(2);
                usersDao.updateUser(users);
                break;
        }
    }

    @Override
    public void OnUserUpdate(int type, int state, String e, Users users) {
        if (state == UsersDao.INSERT_CORRECT){
            data.remove(users);
            adapter.notifyDataSetChanged();
            Toast.makeText(UsersListActivity.this, "Correcto", Toast.LENGTH_SHORT).show();

        }
        else if (state == UsersDao.INSERT_FAILED){
            Toast.makeText(UsersListActivity.this, "Incorrecto", Toast.LENGTH_SHORT).show();

        }

    }
}
