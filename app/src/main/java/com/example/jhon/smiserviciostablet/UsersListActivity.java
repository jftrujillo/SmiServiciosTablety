package com.example.jhon.smiserviciostablet;

import android.app.ProgressDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jhon.smiserviciostablet.Adapters.ListUserAdapter;
import com.example.jhon.smiserviciostablet.Interfaces.QueryInterface;
import com.example.jhon.smiserviciostablet.Models.Users;
import com.example.jhon.smiserviciostablet.Net.UsersDao;
import com.example.jhon.smiserviciostablet.Util.Constants;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    Bundle bundle;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        bundle = getIntent().getExtras();
        try {
            mClient = new MobileServiceClient("https://smiserviciosmovil.azure-mobile.net/",
                    "qIufyUhXNGYkLUXenUUDufQFPMdcUm65",
                    this);

            mTableUsers = mClient.getTable(Users.class);
            usersDao = new UsersDao(mClient,this,this);
            progressDialog = ProgressDialog.show(this,"Recuperando datos","Espere por favor",true,false);
            if (bundle.getBoolean(Constants.IS_ALL_USERS)) {
                usersDao.getAllUsers();
            }

            else {
                usersDao.getAllUsersAvaliable();
            }


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
    progressDialog.dismiss();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        for (Users users : list) {
            if (users.getCreado() != 0) {
            Date date = new Date(users.getCreado());
            String dateString = simpleDateFormat.format(date);
            Log.i("date", dateString);
            }
        }
        if (state == UsersDao.INSERT_CORRECT){
        data = new ArrayList<>();
        data.addAll(list);
        if (bundle.getBoolean(Constants.IS_ALL_USERS)) {
            adapter = new ListUserAdapter(data, this, this,ListUserAdapter.TYPE_ALL);
        }

        else {
            adapter = new ListUserAdapter(data, this, this,ListUserAdapter.TYPE_AVALIABLE);
        }
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return  true;
    }
}
