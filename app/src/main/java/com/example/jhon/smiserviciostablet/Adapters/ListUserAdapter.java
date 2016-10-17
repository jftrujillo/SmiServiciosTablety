package com.example.jhon.smiserviciostablet.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.jhon.smiserviciostablet.Models.Users;
import com.example.jhon.smiserviciostablet.R;

import java.util.List;

/**
 * Created by jhon on 9/08/16.
 */
public class ListUserAdapter extends BaseAdapter {

    List<Users> data;
    Context context;
    OnButtonClickListener onButtonClickListener;
    public static final int ACEPTAR = 0;
    public static final int RECHAZAR = 1;



    public interface OnButtonClickListener{
        void OnButtonclick(int type, Users users);
    }

    public ListUserAdapter(List<Users> data, Context context, OnButtonClickListener onButtonClickListener) {
        this.data = data;
        this.context = context;
        this.onButtonClickListener = onButtonClickListener;
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
        if (view == null){
            v = View.inflate(context, R.layout.users_list_template,null);
        }
        TextView nombre = (TextView) v.findViewById(R.id.nombre);
        TextView direccion = (TextView) v.findViewById(R.id.direccion);
        TextView email = (TextView) v.findViewById(R.id.email);
        TextView telefono = (TextView) v.findViewById(R.id.telefono);
        TextView genero = (TextView) v.findViewById(R.id.genero);
        Button btnAceptar = (Button) v.findViewById(R.id.btn_aceptar);
        Button btnRechazar = (Button) v.findViewById(R.id.btn_rechazar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClickListener.OnButtonclick(ACEPTAR,data.get(i));
            }
        });

        btnRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClickListener.OnButtonclick(RECHAZAR,data.get(i));
            }
        });

        nombre.setText(data.get(i).getName().toString());
        direccion.setText(data.get(i).getAddress().toString());
        email.setText(data.get(i).getMail().toString());
        telefono.setText(data.get(i).getTelephone().toString());
        genero.setText(data.get(i).getGender().toString());
        return v;
    }
}
