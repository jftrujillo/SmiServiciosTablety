package com.example.jhon.smiserviciostablet.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jhon.smiserviciostablet.Models.Users;
import com.example.jhon.smiserviciostablet.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by jhon on 9/08/16.
 */
public class ListUserAdapter extends BaseAdapter {

    List<Users> data;
    Context context;
    OnButtonClickListener onButtonClickListener;
    int type;
    public static final int ACEPTAR = 0;
    public static final int RECHAZAR = 1;
    public static final int TYPE_ALL = 0;
    public static final int TYPE_AVALIABLE = 1;




    public interface OnButtonClickListener{
        void OnButtonclick(int type, Users users);
    }

    public ListUserAdapter(List<Users> data, Context context, OnButtonClickListener onButtonClickListener,int type) {
        this.data = data;
        this.context = context;
        this.onButtonClickListener = onButtonClickListener;
        this.type = type;
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
        LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.promocion_linear);
        linearLayout.setVisibility(View.VISIBLE);
        TextView textPromocion = (TextView) v.findViewById(R.id.promocion);
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
                onButtonClickListener.OnButtonclick(ACEPTAR,data.get(i));
            }
        });

        btnRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClickListener.OnButtonclick(RECHAZAR,data.get(i));
            }
        });
        if (type == TYPE_AVALIABLE ){
            LinearLayout visibilityParent = (LinearLayout) v.findViewById(R.id.linear_visiblitiy);
            visibilityParent.setVisibility(View.GONE);
        }
        TextView textoUno,textoDos,textoTres,textCuatro,textoCinco,textoSeis;
        textoUno = (TextView) v.findViewById(R.id.text1);
        textoDos = (TextView) v.findViewById(R.id.text2);
        textoTres = (TextView) v.findViewById(R.id.text3);
        textCuatro = (TextView) v.findViewById(R.id.text4);
        textoCinco = (TextView) v.findViewById(R.id.text5);
        textoSeis = (TextView) v.findViewById(R.id.text6);

        textoUno.setText("Cédula");
        textoDos.setText("Nombre");
        textoTres.setText("Teléfono");
        textCuatro.setText("Correo electrónico");
        textoCinco.setText("Dirección");
        textoSeis.setText("Género");



        if (data.get(i).getName() != null){
            nombre.setText(data.get(i).getName().equals("") ? "sin nombre" : data.get(i).getName());
        }

        if (data.get(i).getAdress() != null){
            direccion.setText(data.get(i).getAdress().equals("") ? "sin dirección" : data.get(i).getName());
        }

        if (data.get(i).getMail() != null){
            email.setText(data.get(i).getMail());
        }

        if (data.get(i).getTelephone() != null){
            telefono.setText(data.get(i).getCellphone());
        }

        if (data.get(i).getGenre() != null){
            genero.setText(data.get(i).getGenre().equals("") ? "Sin genero específico" : data.get(i).getGenre());
        }
        if (data.get(i).getIdentifycard() != null){
            cedula.setText(data.get(i).getIdentifycard());
        }
        textPromocion.setText(data.get(i).getPromocion() == null ? "Sin código de promocion"
        : data.get(i).getPromocion());

        Date date = new Date(data.get(i).getCreado());
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MMM-dd");
        SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm:ss a");

        if (data.get(i).getCreado() != 0) {
            fechaCreacion.setText(formatDate.format(date));
            tiempoCreacion.setText(formatHour.format(date));
        }
        return v;
    }
}
