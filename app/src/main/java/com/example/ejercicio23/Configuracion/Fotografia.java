package com.example.ejercicio23.Configuracion;

import android.graphics.Bitmap;

public class Fotografia {
    Bitmap imagen;
    String descripcion;

    public Fotografia(Bitmap imagen, String descripcion)
    {
        this.imagen = imagen;
        this.descripcion = descripcion;
    }

    public Bitmap getImagen()
    {
        return imagen;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

}
