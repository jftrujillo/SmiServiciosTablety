package com.example.jhon.smiserviciostablet.Adapters;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.jhon.smiserviciostablet.Models.DriverUserPetition;
import com.example.jhon.smiserviciostablet.Models.RoadUserPetition;
import com.example.jhon.smiserviciostablet.Models.Roadpetitions;
import com.example.jhon.smiserviciostablet.Models.Users;
import com.example.jhon.smiserviciostablet.Models.driverpetitions;
import com.example.jhon.smiserviciostablet.R;

import java.util.List;

/**
 * Created by jhon on 4/09/16.
 */
public class ListRoadPetitionsAdapter extends BaseAdapter{

    List<RoadUserPetition> data;
    Context context;
    List<Users> dataUser;
    Roadpetitions roadpetitions;
    RoadpetitionsInterface roadPetitionsInterface;
    public static final int ACEPTAR = 0;
    public static final int RECHAZAR = 1;



    public interface RoadpetitionsInterface{
        void OnButtonClickListener(int type, RoadUserPetition data,int pos);
    }


    public ListRoadPetitionsAdapter(List<RoadUserPetition> data, Context context, List<Users> dataUser, RoadpetitionsInterface roadPetitionsInterface) {
        this.data = data;
        this.context = context;
        this.dataUser = dataUser;
        this.roadPetitionsInterface = roadPetitionsInterface;
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
                roadPetitionsInterface.OnButtonClickListener(ACEPTAR,data.get(i),i);
            }
        });
        btnRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roadPetitionsInterface.OnButtonClickListener(RECHAZAR,data.get(i),i);
            }
        });
        nombre.setText(data.get(i).getRoadpetitions().getServicename());
        direccion.setText(data.get(i).getRoadpetitions().getLatitude());
        email.setText(data.get(i).getRoadpetitions().getLongitude());
        telefono.setText(data.get(i).getRoadpetitions().getRandomcode());
        genero.setText(data.get(i).getUsers().getGender());
        cedula.setText(data.get(i).getUsers().getIdentifycard());
        return v;
    }
}
