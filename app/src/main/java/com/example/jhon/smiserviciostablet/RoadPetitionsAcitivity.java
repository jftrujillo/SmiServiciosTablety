package com.example.jhon.smiserviciostablet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jhon.smiserviciostablet.Adapters.ListDriverPetitionsAdapter;
import com.example.jhon.smiserviciostablet.Adapters.ListHomePetitionsAdapter;


import com.example.jhon.smiserviciostablet.Adapters.ListRoadPetitionsAdapter;
import com.example.jhon.smiserviciostablet.Interfaces.QueryInterface;
import com.example.jhon.smiserviciostablet.Models.DriverUserPetition;
import com.example.jhon.smiserviciostablet.Models.Homepetitions;
import com.example.jhon.smiserviciostablet.Models.RoadUserPetition;
import com.example.jhon.smiserviciostablet.Models.Roadpetitions;
import com.example.jhon.smiserviciostablet.Models.Users;
import com.example.jhon.smiserviciostablet.Models.driverpetitions;
import com.example.jhon.smiserviciostablet.Net.DriverPetitionsDao;
import com.example.jhon.smiserviciostablet.Net.HomePetitionsDao;
import com.example.jhon.smiserviciostablet.Net.RoadPetitionsDao;
import com.example.jhon.smiserviciostablet.Net.UsersDao;
import com.example.jhon.smiserviciostablet.Util.Constants;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;



public class    RoadPetitionsAcitivity extends AppCompatActivity implements RoadPetitionsDao.QueryInterfaceRoadPetitions, RoadPetitionsDao.UpdateRoadPetitionsInterface, QueryInterface, UsersDao.UsersDaoUpdateInterface, ListRoadPetitionsAdapter.RoadpetitionsInterface, AdapterView.OnItemClickListener {
    Toolbar toobar;
    CollapsingToolbarLayout collapse;
    ListView listView;
    MobileServiceClient mClient;
    MobileServiceTable<Roadpetitions> mTableUsers;
    MobileServiceList<Roadpetitions> mList;
    RoadPetitionsDao roadPetitions;
    UsersDao usersDao;
    List<Users> dataUsers;
    List<Roadpetitions> data;
    List<RoadUserPetition> dataAdapter;
    ListRoadPetitionsAdapter adapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_petitions_acitivity);

        setContentView(R.layout.activity_driver_services);
        setContentView(R.layout.activity_home_petitions);
        data = new ArrayList<>();
        dataUsers = new ArrayList<>();
        dataAdapter = new ArrayList<>();
        progressDialog = ProgressDialog.show(this,"Sincronizando informacion","Por favor espere",true,false);
        try {
            mClient = new MobileServiceClient("https://smiserviciosmovil.azure-mobile.net/",
                    "qIufyUhXNGYkLUXenUUDufQFPMdcUm65",
                    this);

            mTableUsers = mClient.getTable(Roadpetitions.class);
            roadPetitions = new RoadPetitionsDao(mClient,this,this);
            usersDao = new UsersDao(mClient,this,this);
            if (getIntent().getExtras().getInt(DashboardActivity.KIND_PETITION) == DashboardActivity.NORMAL_INTENT) {
                roadPetitions.getAllRoadPetitions();
            }
            else if (getIntent().getExtras().getInt(DashboardActivity.KIND_PETITION) == DashboardActivity.TAKEN_INTENT  ){
                roadPetitions.getTakenRoadPetitions();
            }

        } catch (MalformedURLException e) {
            Toast.makeText(this,"No fue posible conectar con la base de datos",Toast.LENGTH_SHORT).show();
        }

        toobar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(this);
        setSupportActionBar(toobar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Peticiones de Conductor");
        collapse = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapse.setTitle("Peticiones de Asistencia vial");
        collapse.setExpandedTitleColor(getResources().getColor(android.R.color.black));

    }


    @Override
    public void OnQueryFinishRoad(int state, MobileServiceList<Roadpetitions> list) {
        data.addAll(list);
        for (Roadpetitions roadpetitions : data) {
            usersDao.getUserById(roadpetitions.getUserid());
        }
        if (list.size() == 0){
            progressDialog.dismiss();
        }
    }

    @Override
    public void OnUpdateFinishRoad(int state, String e, Roadpetitions roadpetitions, int i) {
        if (state == HomePetitionsDao.INSERT_CORRECT) {
            Intent in = new Intent(Intent.ACTION_SEND);
            in.setType("message/rfc822");
            in.putExtra(Intent.EXTRA_EMAIL, new String[]{dataUsers.get(i).getMail()});
            in.putExtra(Intent.EXTRA_SUBJECT, "Solicitud de SMI Servicios: Rechazada");
            in.putExtra(Intent.EXTRA_TEXT, Constants.REFUSE_EMAIL);
            dataAdapter.remove(i);
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
            try {
                startActivity(Intent.createChooser(in, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "No hay clientes de correo, por favor instale uno.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Error al actualizar la peticion", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    @Override
    public void OnQueryFinish(int state, MobileServiceList<Users> list) {
        if (list.size() > 0 ) {
            dataUsers.add(list.get(0));
        }

        if (dataUsers.size() == data.size()) {
            for (int i = 0; i < data.size(); i++) {
                RoadUserPetition roadUser = new RoadUserPetition();
                roadUser.setRoadpetitions(data.get(i));
                roadUser.setUsers(dataUsers.get(i));
                dataAdapter.add(roadUser);
            }
            adapter = new ListRoadPetitionsAdapter(dataAdapter, this, null, this);
            listView.setAdapter(adapter);
            progressDialog.dismiss();
        }
    }

    @Override
    public void OnUserUpdate(int type, int state, String e, Users users) {

    }

    @Override
    public void OnButtonClickListener(int type, RoadUserPetition data, int pos) {
        switch (type){
            case ListHomePetitionsAdapter.ACEPTAR:
                Intent intent = new Intent(this,AddAsistenceActivity.class);
                intent.putExtra(Constants.KIND_PETITION,Constants.ROAD_PETITION);
                intent.putExtra(Constants.ID_PETITION,data.getRoadpetitions().getId());
                startActivity(intent);
                break;
            case ListHomePetitionsAdapter.RECHAZAR:
                progressDialog = ProgressDialog.show(this,"Rechazando peticion","Por favor espere un momento",true,false);
                data.getRoadpetitions().setState(2);
                roadPetitions.updatePetition(data.getRoadpetitions(),pos);
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this,RoadPetitionDetailActivity.class).putExtra(Constants.ID_PETITION,data.get(position).getId()).putExtra(Constants.ID_USER,dataUsers.get(position).getId()));
    }
}
