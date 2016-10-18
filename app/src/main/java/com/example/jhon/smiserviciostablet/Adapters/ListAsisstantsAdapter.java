package com.example.jhon.smiserviciostablet.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jhon.smiserviciostablet.Models.Assistants;
import com.example.jhon.smiserviciostablet.R;
import com.google.android.gms.vision.text.Text;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jhonfredy on 18/10/2016.
 */

public class ListAsisstantsAdapter extends BaseAdapter {
    List<Assistants> data;
    Context context;

    public ListAsisstantsAdapter(List<Assistants> data, Context context) {
        this.data = data;
        this.context = context;
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
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = view;
        if (v == null){
            v = View.inflate(context, R.layout.template_list_asistant,null);
        }
        ImageView img = (ImageView) v.findViewById(R.id.img);
        Picasso.with(context).load(data.get(i).getUrlimg()).into(img);
        TextView nombre = (TextView) v.findViewById(R.id.nombre);
        nombre.setText(data.get(i).getName());
        TextView cedula = (TextView) v.findViewById(R.id.cedula);
        cedula.setText(data.get(i).getIdentifycard());
        TextView genero = (TextView) v.findViewById(R.id.genero);
        genero.setText(data.get(i).getGender());
        TextView telephone = (TextView) v.findViewById(R.id.telefono);
        telephone.setText(data.get(i).getCellphone());
        return v;
    }
}
