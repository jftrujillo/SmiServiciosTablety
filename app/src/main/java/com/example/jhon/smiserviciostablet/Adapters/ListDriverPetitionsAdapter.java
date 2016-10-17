package com.example.jhon.smiserviciostablet.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.jhon.smiserviciostablet.Models.DriverUserPetition;
import com.example.jhon.smiserviciostablet.Models.driverpetitions;
import com.example.jhon.smiserviciostablet.Models.Users;
import com.example.jhon.smiserviciostablet.Net.DriverPetitionsDao;
import com.example.jhon.smiserviciostablet.R;

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



    public interface HomePetitionsInterface{
        void OnButtonClickListener(int type, DriverUserPetition data,int pos);
    }


    public ListDriverPetitionsAdapter(List<DriverUserPetition> data, Context context, List<Users> dataUser, HomePetitionsInterface homePetitionsInterface) {
        this.data = data;
        this.context = context;
        this.dataUser = dataUser;
        this.homePetitionsInterface = homePetitionsInterface;
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
        nombre.setText(data.get(i).getDriverpetitions1().getServicename());
        direccion.setText(data.get(i).getDriverpetitions1().getFutureaddress());
        email.setText(data.get(i).getDriverpetitions1().getActualaddress());
        telefono.setText(data.get(i).getDriverpetitions1().getDate());
        genero.setText(data.get(i).getUsers().getGender());
        cedula.setText(data.get(i).getUsers().getIdentifycard());
        return v;
    }
}
