package com.example.jhon.smiserviciostablet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.jhon.smiserviciostablet.Interfaces.QueryInterface;
import com.example.jhon.smiserviciostablet.Models.CarBorrow;
import com.example.jhon.smiserviciostablet.Models.Homepetitions;
import com.example.jhon.smiserviciostablet.Models.Users;
import com.example.jhon.smiserviciostablet.Net.CarBorrowDao;
import com.example.jhon.smiserviciostablet.Net.HomePetitionsDao;
import com.example.jhon.smiserviciostablet.Net.UsersDao;
import com.example.jhon.smiserviciostablet.Util.Constants;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;

public class CarBorrowDetailActtivity extends AppCompatActivity implements CarBorrowDao.QueryInterfaceCarBorrowPetitions, CarBorrowDao.UpdateCarBorrowPetitionsInterface, QueryInterface, UsersDao.UsersDaoUpdateInterface {

    Bundle bundle;
    MobileServiceTable<CarBorrow> mTable;
    MobileServiceList<CarBorrow> mList;
    MobileServiceClient mClient;
    CarBorrowDao carBorrowDao;
    UsersDao usersDao;
    Toolbar toolbar;
    EditText nombreCliente,numeroPuestos,fechaInicial,fechaFinal,descrpipcion,cedula,email,telefono;
    RadioButton hombre,mujer;
    Users users;
    CarBorrow carBorrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_borrow_detail_acttivity);
        bundle = getIntent().getExtras();
        try {
            mClient = new MobileServiceClient("https://smiserviciosmovil.azure-mobile.net/",
                    "qIufyUhXNGYkLUXenUUDufQFPMdcUm65",
                    this);

            mTable = mClient.getTable(CarBorrow.class);
            carBorrowDao = new CarBorrowDao(mClient,this,this);
            usersDao = new UsersDao(mClient,this,this);
            carBorrowDao.getCarBorrowPetitionById(bundle.getString(Constants.ID_PETITION));
            usersDao.getUserById(bundle.getString(Constants.ID_USER));
        } catch (MalformedURLException e) {
            Toast.makeText(this,"No fue posible conectar con la base de datos",Toast.LENGTH_SHORT).show();
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detalle de petición");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nombreCliente = (EditText) findViewById(R.id.nombre_cliente);
        numeroPuestos = (EditText) findViewById(R.id.nombre_servicio);
        fechaInicial = (EditText) findViewById(R.id.fecha_inicial);
        fechaFinal = (EditText) findViewById(R.id.fecha_final);
        descrpipcion = (EditText) findViewById(R.id.descripcion);
        cedula = (EditText) findViewById(R.id.cedula_cliente);
        email = (EditText) findViewById(R.id.email);
        hombre = (RadioButton) findViewById(R.id.hombre);
        mujer = (RadioButton) findViewById(R.id.mujer);
        telefono= (EditText) findViewById(R.id.telefono_cliente);
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
    public void OnQueryFinishCarBorrow(int state, MobileServiceList<CarBorrow> list) {
        carBorrow = list.get(0);
        numeroPuestos.setText(carBorrow.getNumberspots());
        fechaInicial.setText(carBorrow.getDatestart());
        descrpipcion.setText(carBorrow.getDescription());
        fechaFinal.setText(carBorrow.getDatefinish());

    }

    @Override
    public void OnUpdateFinishCarBorrow(int state, String e, CarBorrow carBorrow, int i, int type) {

    }

    @Override
    public void OnQueryFinish(int state, MobileServiceList<Users> list) {
        users = list.get(0);
        nombreCliente.setText(users.getName());
        cedula.setText(users.getIdentifycard());
        email.setText(users.getMail());
        telefono.setText(users.getCellphone());
        if (users.getGenre().equals("Mujer")){
            mujer.setChecked(true);
        }
        else {
            hombre.setChecked(true);
        }

    }

    @Override
    public void OnUserUpdate(int type, int state, String e, Users users) {

    }
}
