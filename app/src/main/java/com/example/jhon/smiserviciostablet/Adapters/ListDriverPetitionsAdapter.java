package com.example.jhon.smiserviciostablet.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jhon.smiserviciostablet.Models.DriverUserPetition;
import com.example.jhon.smiserviciostablet.Models.driverpetitions;
import com.example.jhon.smiserviciostablet.Models.Users;
import com.example.jhon.smiserviciostablet.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by jhon on 31/08/16.
 */
public class ListDriverPetitionsAdapter extends BaseAdapter {
    List<DriverUserPetition> data;
    Context context;
    List<Users> dataUser;
    driverpetitions homepetitions;
    HomePetitionsInterface homePetitionsInterface;
    public static final int ACEPTAR = 0;
    public static final int RECHAZAR = 1;
    boolean isTaken;



    public interface HomePetitionsInterface{
        void OnButtonClickListener(int type, DriverUserPetition data,int pos);
    }


    public ListDriverPetitionsAdapter(List<DriverUserPetition> data, Context context, List<Users> dataUser, HomePetitionsInterface homePetitionsInterface, boolean isTaken) {
        this.data = data;
        this.context = context;
        this.dataUser = dataUser;
        this.homePetitionsInterface = homePetitionsInterface;
        this.isTaken = isTaken;
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
        LinearLayout linearFechaAceptada = (LinearLayout) v.findViewById(R.id.linear_fecha_aceptada);
        TextView fechaAceptada = (TextView) v.findViewById(R.id.fecha_aceptada);

        Button btnAceptar = (Button) v.findViewById(R.id.btn_aceptar);
        Button btnRechazar = (Button) v.findViewById(R.id.btn_rechazar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homePetitionsInterface.OnButtonClickListener(ACEPTAR,data.get(i),i);
            }
        });
        btnRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homePetitionsInterface.OnButtonClickListener(RECHAZAR,data.get(i),i);
            }
        });
        TextView textoUno,textoDos,textoTres,textCuatro,textoCinco,textoSeis;
        textoUno = (TextView) v.findViewById(R.id.text1);
        textoDos = (TextView) v.findViewById(R.id.text2);
        textoTres = (TextView) v.findViewById(R.id.text3);
        textCuatro = (TextView) v.findViewById(R.id.text4);
        textoCinco = (TextView) v.findViewById(R.id.text5);
        textoSeis = (TextView) v.findViewById(R.id.text6);


        nombre.setText("No definido");
        direccion.setText(data.get(i).getDriverpetitions1().getFutureaddress());
        email.setText(data.get(i).getDriverpetitions1().getActualaddress());
        telefono.setText(data.get(i).getDriverpetitions1().getDate());
        genero.setText(data.get(i).getUsers().getGenre() );
        cedula.setText(data.get(i).getUsers().getIdentifycard());
        Date date = new Date(data.get(i).getDriverpetitions1().getCreado());
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MMM-dd");
        SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm:ss a");
        SimpleDateFormat formatFechaAceptada = new SimpleDateFormat("yyyy-MMM-dd HH:mm a");



        if (data.get(i).getDriverpetitions1().getCreado() != 0) {
            fechaCreacion.setText(formatDate.format(date));
            tiempoCreacion.setText(formatHour.format(date));
        }

        if (isTaken){
            linearFechaAceptada.setVisibility(View.VISIBLE);
            if (data.get(i).getDriverpetitions1().getFechaaceptada() != 0) {
                fechaAceptada.setText(formatFechaAceptada.format(data.get(i).getDriverpetitions1().getFechaaceptada()));
            }
        }

        textoUno.setText("Cedula");
        textoDos.setText("Servicio");
        textoTres.setText("Fecha");
        textCuatro.setText("Dirección inicial");
        textoCinco.setText("Dirección final");
        textoSeis.setText("Género");
        return v;
    }
}
