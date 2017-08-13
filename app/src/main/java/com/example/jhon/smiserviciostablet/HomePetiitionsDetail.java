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
import com.example.jhon.smiserviciostablet.Models.Users;
import com.example.jhon.smiserviciostablet.Net.HomePetitionsDao;
import com.example.jhon.smiserviciostablet.Net.UsersDao;
import com.example.jhon.smiserviciostablet.Util.Constants;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;

public class HomePetiitionsDetail extends AppCompatActivity implements QueryInterface, HomePetitionsDao.QueryInterfaceHomePetitions, HomePetitionsDao.UpdateHomePetitionsInterface, UsersDao.UsersDaoUpdateInterface {
    Toolbar toolbar;
    EditText nombreServicio,direccion,descrpipcion,codigoAleatorio, nombreCliente, cedula, email;
    RadioButton hombre,mujer;
    Bundle bundle;
    MobileServiceClient mClient;
    MobileServiceTable<Homepetitions> mTableUsers;
    MobileServiceList<Homepetitions> mList;
    HomePetitionsDao homePetitionsDao;
    UsersDao usersDao;
    Users users;
    Homepetitions homepetitions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_petiitions_detail);
        bundle = getIntent().getExtras();
        try {
            mClient = new MobileServiceClient("https://smiserviciosmovil.azure-mobile.net/",
                    "qIufyUhXNGYkLUXenUUDufQFPMdcUm65",
                    this);

            mTableUsers = mClient.getTable(Homepetitions.class);
            homePetitionsDao = new HomePetitionsDao(mClient,this,this);
            usersDao = new UsersDao(mClient,this,this);
            homePetitionsDao.getHomePetitionByID(bundle.getString(Constants.ID_PETITION));
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
            switch (item.getItemId()){
                case android.R.id.home:
                    onBackPressed();
                    break;
            }
            return true;
        }

    @Override
    public void OnQueryFinish(int state, MobileServiceList<Users> list) {
        users = list.get(0);
        nombreCliente.setText(users.getName());
        cedula.setText(users.getIdentifycard());
        email.setText(users.getMail());
        if (users.getGenre().equals("Mujer")){
            mujer.setChecked(true);
        }
        else {
            hombre.setChecked(true);
        }
    }

    @Override
    public void OnQueryFinishHome(int state, MobileServiceList<Homepetitions> list) {
        homepetitions = list.get(0);
        nombreServicio.setText(homepetitions.getServicename());
        direccion.setText(homepetitions.getAddress());
        descrpipcion.setText(homepetitions.getDescription());
        codigoAleatorio.setText(homepetitions.getRandomcode());
    }


    @Override
    public void OnUpdateFinishHome(int state, String e, Homepetitions homepetitions, int i) {

    }

    @Override
    public void OnUserUpdate(int type, int state, String e, Users users) {

    }


}
