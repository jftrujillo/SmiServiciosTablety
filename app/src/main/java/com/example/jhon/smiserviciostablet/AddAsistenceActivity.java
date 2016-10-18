
package com.example.jhon.smiserviciostablet;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jhon.smiserviciostablet.Adapters.ListAsisstantsAdapter;
import com.example.jhon.smiserviciostablet.Models.Assistants;
import com.example.jhon.smiserviciostablet.Models.Homepetitions;
import com.example.jhon.smiserviciostablet.Models.Roadpetitions;
import com.example.jhon.smiserviciostablet.Models.driverpetitions;
import com.example.jhon.smiserviciostablet.Net.AssistantDao;
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

public class AddAsistenceActivity extends AppCompatActivity implements AssistantDao.QueryInterfaceAssistant, AdapterView.OnItemClickListener, HomePetitionsDao.QueryInterfaceHomePetitions, HomePetitionsDao.UpdateHomePetitionsInterface, RoadPetitionsDao.QueryInterfaceRoadPetitions, RoadPetitionsDao.UpdateRoadPetitionsInterface, DriverPetitionsDao.QueryInterfaceDriverPetitions, DriverPetitionsDao.UpdateDriverPetitionsInterface {
    Toolbar toolbar;
    ListView listView;
    MobileServiceClient mClient;
    MobileServiceTable<Assistants> mTable;
    MobileServiceList<Assistants> mList;
    AssistantDao assistantDao;
    ListAsisstantsAdapter adapter;
    List<Assistants> data;
    ProgressDialog progressDialog;
    Bundle bundle;
    String asistantId;
    HomePetitionsDao homePetitionsDao;
    RoadPetitionsDao roadPetitionDao;
    DriverPetitionsDao driverDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_asistence);
        bundle = getIntent().getExtras();
        progressDialog = ProgressDialog.show(this,"Recuperando lista de asistentes","Por favor espere",true,false);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Asigna un encargado");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.list);
        data = new ArrayList<>();

        try {
            mClient = new MobileServiceClient("https://smiserviciosmovil.azure-mobile.net/",
                    "qIufyUhXNGYkLUXenUUDufQFPMdcUm65",
                    this);

            mTable = mClient.getTable(Assistants.class);
            assistantDao = new AssistantDao(mClient,this);
            assistantDao.getAllAsisstant();

        } catch (MalformedURLException e) {
            Toast.makeText(this,"No fue posible conectar con la base de datos",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void OnQueryFinishAsistant(int state, MobileServiceList<Assistants> list) {
        if (state == AssistantDao.INSERT_CORRECT){
            data.addAll(list);
            adapter = new ListAsisstantsAdapter(list,this);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
            progressDialog.dismiss();
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(this,"Error recuperando la lista de asistentes, por favor intentelo nuevamente",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        asistantId = data.get(i).getId();
        switch (bundle.getInt(Constants.KIND_PETITION)){
            case Constants.HOME_PETITION:
                progressDialog = ProgressDialog.show(this,"Aignando asistente a esta peticion","por favor espere",true,false);
                homePetitionsDao = new HomePetitionsDao(mClient,this,this);
                homePetitionsDao.getHomePetitionByID(bundle.getString(Constants.ID_PETITION));
                break;

            case Constants.ROAD_PETITION:
                progressDialog = ProgressDialog.show(this,"Aignando asistente a esta peticion","por favor espere",true,false);
                roadPetitionDao = new RoadPetitionsDao(mClient,this,this);
                roadPetitionDao.getRoadPetitionById(bundle.getString(Constants.ID_PETITION));
                break;

            case Constants.DRIVER_PETITION:
                progressDialog = ProgressDialog.show(this,"Aignando asistente a esta peticion","por favor espere",true,false);
                driverDao = new DriverPetitionsDao(mClient,this,this);
                driverDao.getDriverPetitionById(bundle.getString(Constants.ID_PETITION));
                break;
        }
    }

    @Override
    public void OnQueryFinishHome(int state, MobileServiceList<Homepetitions> list) {
        if (state == HomePetitionsDao.INSERT_CORRECT){
            Homepetitions homePeition = list.get(0);
            homePeition.setState(Homepetitions.PETITION_TAKEN);
            homePeition.setSupport_person(asistantId);
            homePetitionsDao.updatePetition(homePeition);
            finish();
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(this, "Error asignado asistente", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnUpdateFinishHome(int state, String e, Homepetitions homepetitions, int i) {
        if (state == HomePetitionsDao.INSERT_CORRECT){
            Toast.makeText(AddAsistenceActivity.this, "Se asigno con exito el asistente", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(AddAsistenceActivity.this, "Problemas asignando encargado, intente nuevamente por favor", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void OnQueryFinishRoad(int state, MobileServiceList<Roadpetitions> list) {
        if (state == HomePetitionsDao.INSERT_CORRECT){
            Roadpetitions roadPetition = list.get(0);
            roadPetition.setSupport_person(asistantId);
            roadPetition.setState(Roadpetitions.PETITION_TAKEN);
            roadPetitionDao.updatePetition(roadPetition,0);
            finish();
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(this, "Error asignado asistente", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnUpdateFinishRoad(int state, String e, Roadpetitions roadpetitions, int i) {
        if (state == RoadPetitionsDao.INSERT_CORRECT){
            Toast.makeText(AddAsistenceActivity.this, "Se asigno con exito el asistente", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(AddAsistenceActivity.this, "Problemas asignando encargado, intente nuevamente por favor", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnQueryFinishDriver(int state, MobileServiceList<driverpetitions> list) {
        if (state == HomePetitionsDao.INSERT_CORRECT){
            driverpetitions driver = list.get(0);
            driver.setSupport_person(asistantId);
            driver.setState(driverpetitions.PETITION_TAKEN);
            driverDao.updatePetition(driver,0);
            finish();
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(this, "Error asignado asistente", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnUpdateFinishDriver(int state, String e, driverpetitions homepetitions, int i) {
        if (state == RoadPetitionsDao.INSERT_CORRECT){
            Toast.makeText(AddAsistenceActivity.this, "Se asigno con exito el asistente", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(AddAsistenceActivity.this, "Problemas asignando encargado, intente nuevamente por favor", Toast.LENGTH_SHORT).show();
        }
    }
}
