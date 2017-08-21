
package com.example.jhon.smiserviciostablet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jhon.smiserviciostablet.Adapters.ListAsisstantsAdapter;
import com.example.jhon.smiserviciostablet.Interfaces.QueryInterface;
import com.example.jhon.smiserviciostablet.Models.Assistants;
import com.example.jhon.smiserviciostablet.Models.CarBorrow;
import com.example.jhon.smiserviciostablet.Models.Homepetitions;
import com.example.jhon.smiserviciostablet.Models.Roadpetitions;
import com.example.jhon.smiserviciostablet.Models.Users;
import com.example.jhon.smiserviciostablet.Models.driverpetitions;
import com.example.jhon.smiserviciostablet.Net.AssistantDao;
import com.example.jhon.smiserviciostablet.Net.CarBorrowDao;
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
import java.util.Date;
import java.util.List;

public class AddAsistenceActivity extends AppCompatActivity implements AssistantDao.QueryInterfaceAssistant, AdapterView.OnItemClickListener, HomePetitionsDao.QueryInterfaceHomePetitions, HomePetitionsDao.UpdateHomePetitionsInterface, RoadPetitionsDao.QueryInterfaceRoadPetitions, RoadPetitionsDao.UpdateRoadPetitionsInterface, DriverPetitionsDao.QueryInterfaceDriverPetitions, DriverPetitionsDao.UpdateDriverPetitionsInterface, QueryInterface, UsersDao.UsersDaoUpdateInterface, CarBorrowDao.QueryInterfaceCarBorrowPetitions, CarBorrowDao.UpdateCarBorrowPetitionsInterface {
    Toolbar toolbar;
    ListView listView;
    MobileServiceClient mClient;
    MobileServiceTable<Assistants> mTable;
    MobileServiceList<Assistants> mList;
    Assistants assistant;
    AssistantDao assistantDao;
    ListAsisstantsAdapter adapter;
    List<Assistants> data;
    ProgressDialog progressDialog;
    Bundle bundle;
    String asistantId;
    HomePetitionsDao homePetitionsDao;
    RoadPetitionsDao roadPetitionDao;
    DriverPetitionsDao driverDao;
    CarBorrowDao carBorrowDao;
    Users user;
    UsersDao usersDao;

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
            usersDao = new UsersDao(mClient,this,this);

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
        assistant = data.get(i);
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
            case Constants.CAR_BORROW_PETITION:
                progressDialog = ProgressDialog.show(this,"Aignando asistente a esta peticion","por favor espere",true,false);
                carBorrowDao = new CarBorrowDao(mClient,this,this);
                carBorrowDao.getCarBorrowPetitionById(bundle.getString(Constants.ID_PETITION));
                break;
        }
    }

    @Override
    public void OnQueryFinishHome(int state, MobileServiceList<Homepetitions> list) {
        if (state == HomePetitionsDao.INSERT_CORRECT){
            Homepetitions homePeition = list.get(0);
            homePeition.setState(Homepetitions.PETITION_TAKEN);
            homePeition.setSupport_person(asistantId);
            homePeition.setFechaaceptada(new Date().getTime());
            usersDao.getUserById(homePeition.getUserid());
            homePetitionsDao.updatePetition(homePeition);

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
//            progressDialog.dismiss();
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
            roadPetition.setFechaaceptada(new Date().getTime());
            roadPetitionDao.updatePetition(roadPetition,0);
            usersDao.getUserById(roadPetition.getUserid());
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(this, "Error asignado asistente", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnUpdateFinishRoad(int state, String e, Roadpetitions roadpetitions, int i) {
        if (state == RoadPetitionsDao.INSERT_CORRECT){
            Toast.makeText(AddAsistenceActivity.this, "Se asignó con éxito el asistente", Toast.LENGTH_SHORT).show();
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
            driver.setFechaaceptada(new Date().getTime());
            driverDao.updatePetition(driver,0);
            usersDao.getUserById(driver.getUserid());
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(this, "Error asignado asistente", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnUpdateFinishDriver(int state, String e, driverpetitions homepetitions, int i) {
        if (state == RoadPetitionsDao.INSERT_CORRECT){
            Toast.makeText(AddAsistenceActivity.this, "Se asignó con éxito el asistente", Toast.LENGTH_SHORT).show();
//            progressDialog.dismiss();
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(AddAsistenceActivity.this, "Problemas asignando encargado, intente nuevamente por favor", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnQueryFinish(int state, MobileServiceList<Users> list) {
        if (state == HomePetitionsDao.INSERT_CORRECT){
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{list.get(0).getMail()});
            i.putExtra(Intent.EXTRA_SUBJECT, "Solicitud de SMI Servicios: Aceptado");
            i.putExtra(Intent.EXTRA_TEXT   , "Su solicitud ha sido aceptada, muchas gracias por preferirnos. " +
                    "En minutos llegará alguien  para ayudarlo. El nombre del encargado que atenderá " +
                    "su solicitud es " + assistant.getName() + " identificado con número de cédula: " + assistant.getIdentifycard()+
                    " se envía fotografía del asistente " + assistant.getUrlimg());
            try {
                startActivity(Intent.createChooser(i, "Creando correo electrónico..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "No hay clientes de correo, por favor instale uno.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "No hay usuarios ligados a esta solicitud", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    public void OnUserUpdate(int type, int state, String e, Users users) {

    }

    @Override
    public void OnQueryFinishCarBorrow(int state, MobileServiceList<CarBorrow> list) {
        if (state == HomePetitionsDao.INSERT_CORRECT){
            CarBorrow carBorrow = list.get(0);
            carBorrow.setState(Homepetitions.PETITION_TAKEN);
            carBorrow.setFechaaceptada(new Date().getTime());
            usersDao.getUserById(carBorrow.getUserid());
            carBorrowDao.updatePetition(carBorrow,0,0);

        }
        else {
            progressDialog.dismiss();
            Toast.makeText(this, "Error asignado asistente", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void OnUpdateFinishCarBorrow(int state, String e, CarBorrow carBorrow, int i, int type) {
        if (state == RoadPetitionsDao.INSERT_CORRECT){
            Toast.makeText(AddAsistenceActivity.this, "Se asignó con éxito el asistente", Toast.LENGTH_SHORT).show();
//            progressDialog.dismiss();
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(AddAsistenceActivity.this, "Problemas asignando encargado, intente nuevamente por favor", Toast.LENGTH_SHORT).show();
        }
    }
}
