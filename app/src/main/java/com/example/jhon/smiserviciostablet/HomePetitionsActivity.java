package com.example.jhon.smiserviciostablet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jhon.smiserviciostablet.Adapters.ListHomePetitionsAdapter;
import com.example.jhon.smiserviciostablet.Adapters.ListUserAdapter;
import com.example.jhon.smiserviciostablet.Interfaces.QueryInterface;
import com.example.jhon.smiserviciostablet.Models.Homepetitions;
import com.example.jhon.smiserviciostablet.Models.Users;
import com.example.jhon.smiserviciostablet.Net.HomePetitionsDao;
import com.example.jhon.smiserviciostablet.Net.UsersDao;
import com.example.jhon.smiserviciostablet.Util.Constants;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomePetitionsActivity extends AppCompatActivity implements HomePetitionsDao.QueryInterfaceHomePetitions, QueryInterface, ListHomePetitionsAdapter.HomePetitionsInterface, UsersDao.UsersDaoUpdateInterface, HomePetitionsDao.UpdateHomePetitionsInterface, AdapterView.OnItemClickListener {
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
    ProgressDialog progressDialog;
    boolean isTaken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_petitions);
        data = new ArrayList<>();
        dataUsers = new ArrayList<>();
        progressDialog = ProgressDialog.show(this,"Sincronizando informacion","Por favor espere",true,false);
        try {
            mClient = new MobileServiceClient("https://smiserviciosmovil.azure-mobile.net/",
                    "qIufyUhXNGYkLUXenUUDufQFPMdcUm65",
                    this);

            mTableUsers = mClient.getTable(Homepetitions.class);
            homePetitionsDao = new HomePetitionsDao(mClient,this,this);
            usersDao = new UsersDao(mClient,this,this);
            if (getIntent().getExtras().getInt(DashboardActivity.KIND_PETITION) == DashboardActivity.NORMAL_INTENT) {
                homePetitionsDao.getAllHomePetitions();
                isTaken = false;
            }
            else if (getIntent().getExtras().getInt(DashboardActivity.KIND_PETITION) == DashboardActivity.TAKEN_INTENT){
                homePetitionsDao.getTakenHomePetitions();
                isTaken = true;
            }

        } catch (MalformedURLException e) {
            Toast.makeText(this,"No fue posible conectar con la base de datos",Toast.LENGTH_SHORT).show();
        }

        toobar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(this);
        setSupportActionBar(toobar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Peticiones de hogar");
        collapse = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapse.setTitle("Peticiones del hogar");
        collapse.setExpandedTitleColor(getResources().getColor(android.R.color.black));
        collapse.setCollapsedTitleTextColor(getResources().getColor(android.R.color.black));
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void OnQueryFinishHome(int state, MobileServiceList<Homepetitions> list) {
        data.addAll(list);
        for (Homepetitions homepetitions : data) {
            usersDao.getUserById(homepetitions.getUserid());
        }
        if (list.size() == 0){
            progressDialog.dismiss();
        }
    }

    @Override
    public void OnQueryFinish(int state, MobileServiceList<Users> list) {
        progressDialog.dismiss();
        if (list.size() > 0) {
            dataUsers.add(list.get(0));
        }
        if (dataUsers.size() == data.size()){
            adapter = new ListHomePetitionsAdapter(data,this,dataUsers,this,isTaken);
            listView.setAdapter(adapter);
            progressDialog.dismiss();
        }
    }

    @Override
    public void OnButtonClickListener(int type, Homepetitions homepetitions, int i) {
        switch (type){
            case ListHomePetitionsAdapter.ACEPTAR:
                Intent intent = new Intent(this,AddAsistenceActivity.class);
                intent.putExtra(Constants.KIND_PETITION,Constants.HOME_PETITION);
                intent.putExtra(Constants.ID_PETITION,homepetitions.getId());
                startActivity(intent);
                break;
            case ListHomePetitionsAdapter.RECHAZAR:
                progressDialog = ProgressDialog.show(this,"Rechazando peticion","Por favor espere un momento",true,false);
                homepetitions.setState(2);
                homepetitions.setFechaaceptada(new Date().getTime());
                homePetitionsDao.updatePetition(homepetitions);
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.setType("message/rfc822");
                intent1.putExtra(Intent.EXTRA_EMAIL  , new String[]{dataUsers.get(i).getMail()});
                intent1.putExtra(Intent.EXTRA_SUBJECT, "Solicitud de SMI Servicios: Rechazada");
                intent1.putExtra(Intent.EXTRA_TEXT   , Constants.REFUSE_EMAIL);
                try {
                    startActivity(Intent.createChooser(intent1, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "No hay clientes de correo, por favor instale uno.", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    @Override
    public void OnUserUpdate(int type, int state, String e,Users users) {
    }

    @Override
    public void OnUpdateFinishHome(int state, String e,Homepetitions homepetitions, int i) {
        progressDialog.dismiss();
        if (state == HomePetitionsDao.INSERT_CORRECT){
            data.remove(homepetitions);
            for (Homepetitions homepetitionsdata : data) {
                usersDao.getUserById(homepetitionsdata.getUserid());
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(HomePetitionsActivity.this,HomePetiitionsDetail.class).putExtra(Constants.ID_PETITION,data.get(position).getId()).putExtra(Constants.ID_USER,dataUsers.get(position).getId()));
    }
}
