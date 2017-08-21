package com.example.jhon.smiserviciostablet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jhon.smiserviciostablet.Adapters.ListBarBorrowAdapter;
import com.example.jhon.smiserviciostablet.Adapters.ListHomePetitionsAdapter;
import com.example.jhon.smiserviciostablet.Interfaces.QueryInterface;
import com.example.jhon.smiserviciostablet.Models.CarBorrow;
import com.example.jhon.smiserviciostablet.Models.UserBorrowCar;
import com.example.jhon.smiserviciostablet.Models.Users;
import com.example.jhon.smiserviciostablet.Net.CarBorrowDao;
import com.example.jhon.smiserviciostablet.Net.HomePetitionsDao;
import com.example.jhon.smiserviciostablet.Net.UsersDao;
import com.example.jhon.smiserviciostablet.Util.Constants;
import com.google.android.gms.drive.query.internal.InFilter;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CarBorrowActivity extends AppCompatActivity implements CarBorrowDao.QueryInterfaceCarBorrowPetitions, CarBorrowDao.UpdateCarBorrowPetitionsInterface, QueryInterface, UsersDao.UsersDaoUpdateInterface, ListBarBorrowAdapter.CarBorrowPetitionsInterface {
    Toolbar toobar;
    CollapsingToolbarLayout collapse;
    ListView listView;
    MobileServiceClient mClient;
    MobileServiceTable<CarBorrow> mTableUsers;
    CarBorrowDao carBorrowDao;
    UsersDao usersDao;
    List<Users> dataUsers;
    List<CarBorrow> data;
    List<UserBorrowCar> dataAdapter;
    ListBarBorrowAdapter adapter;
    ProgressDialog progressDialog;
    boolean isTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_borrow);
        progressDialog = ProgressDialog.show(this,"Sincronizando información","Por favor espere",true,false);
        data = new ArrayList<>();
        dataUsers = new ArrayList<>();
        dataAdapter = new ArrayList<>();

        try {
            mClient = new MobileServiceClient("https://smiserviciosmovil.azure-mobile.net/",
                    "qIufyUhXNGYkLUXenUUDufQFPMdcUm65",
                    this);

            mTableUsers = mClient.getTable(CarBorrow.class);
            carBorrowDao = new CarBorrowDao(mClient,this,this);
            usersDao = new UsersDao(mClient,this,this);
            if (getIntent().getExtras().getInt(DashboardActivity.KIND_PETITION) == DashboardActivity.NORMAL_INTENT) {
                carBorrowDao.getAllCarBorrowPetitions();
                isTaken = false;
            }
            else if (getIntent().getExtras().getInt(DashboardActivity.KIND_PETITION) == DashboardActivity.TAKEN_INTENT){
                carBorrowDao.getTakenCarBorrowPetitions();
                isTaken = true;
            }

        } catch (MalformedURLException e) {
            Toast.makeText(this,"No fue posible conectar con la base de datos",Toast.LENGTH_SHORT).show();
        }


        toobar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.list_view);
        setSupportActionBar(toobar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Peticiones de alquiler de vehículo");
        collapse = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapse.setTitle("Peticiones de alquiler de vehículo");
        collapse.setExpandedTitleColor(getResources().getColor(android.R.color.black));

    }

    @Override
    public void OnQueryFinishCarBorrow(int state, MobileServiceList<CarBorrow> list) {
        data.addAll(list);
        for (CarBorrow carBorrow : data) {
            usersDao.getUserById(carBorrow.getUserid());
        }
        if (list.size() == 0){
            progressDialog.dismiss();
        }
    }

    @Override
    public void OnUpdateFinishCarBorrow(int state, String e, CarBorrow carBorrow, int i, int type) {
        if (state == HomePetitionsDao.INSERT_CORRECT){
            if (type == CarBorrowDao.RECHAZAR) {

                progressDialog.dismiss();
                dataAdapter.remove(i);
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{dataUsers.get(i).getMail()});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Solicitud de SMI Servicios: Rechazada");
                intent.putExtra(Intent.EXTRA_TEXT, Constants.REFUSE_EMAIL);
                try {
                    startActivity(Intent.createChooser(intent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "No hay clientes de correo, por favor instale uno.", Toast.LENGTH_SHORT).show();
                }
            }
            if (type == CarBorrowDao.ACEPTAR) {
                progressDialog.dismiss();
                dataAdapter.remove(i);
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{dataUsers.get(i).getMail()});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Solicitud de SMI Servicios: Aceptada");
                intent.putExtra(Intent.EXTRA_TEXT, "Muchas gracias por usar nuestra aplicación. En un momento nos comunicaremos con usted para confirmar el alquiler del vehiculo");
                try {
                    startActivity(Intent.createChooser(intent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "No hay clientes de correo, por favor instale uno.", Toast.LENGTH_SHORT).show();
                }
            }

        }
        else {
            Toast.makeText(this, "Error al actualizar la peticion", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    @Override
    public void OnQueryFinish(int state, MobileServiceList<Users> list) {
        if(list.size() > 0) {
            dataUsers.add(list.get(0));
        }
        if (dataUsers.size() == data.size()){
            for (int i = 0; i < data.size(); i++) {
                UserBorrowCar userBorrowCar = new UserBorrowCar();
                userBorrowCar.setCarBorrow(data.get(i));
                userBorrowCar.setUsers(dataUsers.get(i));
                dataAdapter.add(userBorrowCar);
            }
            adapter = new ListBarBorrowAdapter(dataAdapter,this,null,this,isTaken);
            listView.setAdapter(adapter);
            progressDialog.dismiss();
        }
    }

    @Override
    public void OnUserUpdate(int type, int state, String e, Users users) {

    }

    @Override
    public void OnButtonClickListener(int type, UserBorrowCar data, int pos) {
        switch (type){
            case ListHomePetitionsAdapter.ACEPTAR:
                /*Intent intent = new Intent(this,AddAsistenceActivity.class);
                intent.putExtra(Constants.KIND_PETITION,Constants.CAR_BORROW_PETITION);
                intent.putExtra(Constants.ID_PETITION,data.getCarBorrow().getId());
                startActivity(intent);
                */
                data.getCarBorrow().setState(1);
                data.getCarBorrow().setFechaaceptada(new Date().getTime());
                carBorrowDao.updatePetition(data.getCarBorrow(),pos,CarBorrowDao.ACEPTAR);
                break;
            case ListHomePetitionsAdapter.RECHAZAR:
                data.getCarBorrow().setState(2);
                data.getCarBorrow().setFechaaceptada(new Date().getTime());
                carBorrowDao.updatePetition(data.getCarBorrow(),pos,CarBorrowDao.RECHAZAR);
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
