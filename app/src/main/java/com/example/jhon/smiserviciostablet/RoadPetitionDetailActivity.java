package com.example.jhon.smiserviciostablet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.jhon.smiserviciostablet.Interfaces.QueryInterface;
import com.example.jhon.smiserviciostablet.Models.Homepetitions;
import com.example.jhon.smiserviciostablet.Models.Roadpetitions;
import com.example.jhon.smiserviciostablet.Models.Users;
import com.example.jhon.smiserviciostablet.Net.HomePetitionsDao;
import com.example.jhon.smiserviciostablet.Net.RoadPetitionsDao;
import com.example.jhon.smiserviciostablet.Net.UsersDao;
import com.example.jhon.smiserviciostablet.Util.Constants;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;


public class RoadPetitionDetailActivity extends AppCompatActivity implements RoadPetitionsDao.QueryInterfaceRoadPetitions, RoadPetitionsDao.UpdateRoadPetitionsInterface, QueryInterface, UsersDao.UsersDaoUpdateInterface {

    Toolbar toolbar;
    EditText nombreServicio,direccion,descrpipcion,codigoAleatorio, nombreCliente, cedula, email;
    RadioButton hombre,mujer;
    Bundle bundle;
    MobileServiceClient mClient;
    MobileServiceTable<Roadpetitions> mTableUsers;
    MobileServiceList<Roadpetitions> mList;
    RoadPetitionsDao roadPetitionsDao;
    UsersDao usersDao;
    Users users;
    Roadpetitions roadpetitions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_petition_detail);


        bundle = getIntent().getExtras();
        try {
            mClient = new MobileServiceClient("https://smiserviciosmovil.azure-mobile.net/",
                    "qIufyUhXNGYkLUXenUUDufQFPMdcUm65",
                    this);

            mTableUsers = mClient.getTable(Roadpetitions.class);
            roadPetitionsDao = new RoadPetitionsDao(mClient,this,this);
            usersDao = new UsersDao(mClient,this,this);
            roadPetitionsDao.getRoadPetitionById(bundle.getString(Constants.ID_PETITION));
            usersDao.getUserById(bundle.getString(Constants.ID_USER));
        } catch (MalformedURLException e) {
            Toast.makeText(this,"No fue posible conectar con la base de datos",Toast.LENGTH_SHORT).show();
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detalle de peticion");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nombreCliente = (EditText) findViewById(R.id.nombre_cliente);
        nombreServicio = (EditText) findViewById(R.id.nombre_servicio);
        direccion = (EditText) findViewById(R.id.direccion);
        descrpipcion = (EditText) findViewById(R.id.descripcion);
        codigoAleatorio = (EditText) findViewById(R.id.codigo_aleatorio);
        cedula = (EditText) findViewById(R.id.cedula_cliente);
        email = (EditText) findViewById(R.id.email);
        hombre = (RadioButton) findViewById(R.id.hombre);
        mujer = (RadioButton) findViewById(R.id.mujer);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnQueryFinishRoad(int state, MobileServiceList<Roadpetitions> list) {
        roadpetitions = list.get(0);
        nombreServicio.setText(roadpetitions.getServicename());
        direccion.setText("Latitude "+ roadpetitions.getLatitude() + " Longitud: " + roadpetitions.getLongitude());
        descrpipcion.setText(roadpetitions.getDescription());
        codigoAleatorio.setText(roadpetitions.getRandomcode());
    }

    @Override
    public void OnUpdateFinishRoad(int state, String e, Roadpetitions roadpetitions, int i) {

    }

    @Override
    public void OnQueryFinish(int state, MobileServiceList<Users> list) {
        users = list.get(0);
        nombreCliente.setText(users.getName());
        cedula.setText(users.getIdentifycard());
        email.setText(users.getMail());
        if (users.getGenre() != null && users.getGenre().equals("Mujer")){
            mujer.setChecked(true);
        }
        else if (users.getGenre() != null) {
            hombre.setChecked(true);
        }
    }

    @Override
    public void OnUserUpdate(int type, int state, String e, Users users) {

    }
}
