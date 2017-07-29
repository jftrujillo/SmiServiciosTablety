package com.example.jhon.smiserviciostablet.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.jhon.smiserviciostablet.Models.CarBorrow;
import com.example.jhon.smiserviciostablet.Models.UserBorrowCar;
import com.example.jhon.smiserviciostablet.Models.Users;
import com.example.jhon.smiserviciostablet.R;
import com.google.android.gms.vision.text.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by jhon on 12/29/16.
 */

public class ListBarBorrowAdapter extends BaseAdapter
{
    List<UserBorrowCar> data;
    Context context;
    List<Users> dataUser;
    CarBorrow carBorrow;
    CarBorrowPetitionsInterface CarBorrowPetitionsInterface;
    public static final int ACEPTAR = 0;
    public static final int RECHAZAR = 1;



    public interface CarBorrowPetitionsInterface{
        void OnButtonClickListener(int type, UserBorrowCar data,int pos);
    }


    public ListBarBorrowAdapter(List<UserBorrowCar> data, Context context, List<Users> dataUser, CarBorrowPetitionsInterface CarBorrowPetitionsInterface) {
        this.data = data;
        this.context = context;
        this.dataUser = dataUser;
        this.CarBorrowPetitionsInterface = CarBorrowPetitionsInterface;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null){
            v = View.inflate(context, R.layout.users_list_template,null);
        }

        TextView nombre = (TextView) v.findViewById(R.id.nombre);
        TextView direccion = (TextView) v.findViewById(R.id.direccion);
        TextView email = (TextView) v.findViewById(R.id.email);
        TextView telefono = (TextView) v.findViewById(R.id.telefono);
        TextView genero = (TextView) v.findViewById(R.id.genero);
        TextView cedula = (TextView) v.findViewById(R.id.cedula);
        TextView fechaCreacion = (TextView) v.findViewById(R.id.fecha_creacion);
        TextView tiempoCreacion = (TextView) v.findViewById(R.id.tiempo_creacion);
        Button btnAceptar = (Button) v.findViewById(R.id.btn_aceptar);
        Button btnRechazar = (Button) v.findViewById(R.id.btn_rechazar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarBorrowPetitionsInterface.OnButtonClickListener(ACEPTAR,data.get(i),i);
            }
        });
        btnRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarBorrowPetitionsInterface.OnButtonClickListener(RECHAZAR,data.get(i),i);
            }
        });
        TextView textoUno,textoDos,textoTres,textCuatro,textoCinco,textoSeis;
        textoUno = (TextView) v.findViewById(R.id.text1);
        textoDos = (TextView) v.findViewById(R.id.text2);
        textoTres = (TextView) v.findViewById(R.id.text3);
        textCuatro = (TextView) v.findViewById(R.id.text4);
        textoCinco = (TextView) v.findViewById(R.id.text5);
        textoSeis = (TextView) v.findViewById(R.id.text6);


        nombre.setText(data.get(i).getCarBorrow().getDescription());
        direccion.setText(data.get(i).getCarBorrow().getDatestart());
        email.setText(data.get(i).getCarBorrow().getDatefinish());
        telefono.setText(data.get(i).getCarBorrow().getNumberspots());
        genero.setText(data.get(i).getUsers().getGenre().equals("") ? "Sin género definido" : data.get(i).getUsers().getGenre());
        cedula.setText(data.get(i).getUsers().getIdentifycard());
        Date date = new Date(data.get(i).getCarBorrow().getCreado());
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MMM-dd");
        SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm:ss a");

        if (data.get(i).getCarBorrow().getCreado() != 0){
            fechaCreacion.setText(formatDate.format(date));
            tiempoCreacion.setText(formatHour.format(date));
        }

        textoUno.setText("Cédula");
        textoDos.setText("Descripción");
        textoTres.setText("No de Puestos");
        textCuatro.setText("Fecha final");
        textoCinco.setText("Fecha inicial");
        textoSeis.setText("Género");

        return v;
    }
}
