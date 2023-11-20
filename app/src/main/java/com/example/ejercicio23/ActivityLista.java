package com.example.ejercicio23;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ListView;

import com.example.ejercicio23.Configuracion.Adapter;
import com.example.ejercicio23.Configuracion.Fotografia;
import com.example.ejercicio23.Configuracion.SQLiteConexion;
import com.example.ejercicio23.Configuracion.Tabla;

import java.util.ArrayList;

public class ActivityLista extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        ListView simpleGridView = (ListView) findViewById(R.id.listView);

        SQLiteConexion help = new SQLiteConexion(this, Tabla.NameDataBase, null, 1);
        ArrayList<Fotografia> listaImagenes = new ArrayList<>();
        Cursor c= help.getAll();
        int i=0;
        if(c.getCount()>0)
        {
            c.moveToFirst();
            while(c.isAfterLast()==false)
            {

                byte[] bytes=c.getBlob(c.getColumnIndex(Tabla.blobImagen));
                String descripcion=c.getString(c.getColumnIndex(Tabla.descripcion));

                Fotografia fotografia = new Fotografia(BitmapFactory.decodeByteArray(bytes, 0, bytes.length), descripcion);
                listaImagenes.add(fotografia);
                c.moveToNext();
                i++;
            }

            Adapter myAdapter=new Adapter(this,R.layout.ver_lista,listaImagenes);
            simpleGridView.setAdapter(myAdapter);
        }
    }
}