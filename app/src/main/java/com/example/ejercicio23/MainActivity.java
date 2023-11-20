package com.example.ejercicio23;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ejercicio23.Configuracion.SQLiteConexion;
import com.example.ejercicio23.Configuracion.Tabla;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
    Button btnFoto;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PETICION_ACCESO_CAM = 100;
    ImageView objImagen;
    SQLiteConexion BD;
    byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        objImagen = (ImageView) findViewById(R.id.foto);
        btnFoto = (Button) findViewById(R.id.btnFoto);
        BD = new SQLiteConexion(getApplicationContext(), Tabla.NameDataBase, null, 1);
        byteArray = new byte[0];
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });

        EditText descripcion = (EditText) findViewById(R.id.txtDescripcion);
        Button btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(byteArray.length != 0) {
                    BD.insert(byteArray, descripcion.getText().toString());
                    Toast.makeText(getApplicationContext(), "GUARDADO CON EXITO", Toast.LENGTH_LONG).show();
                    byteArray = new byte[0];
                    objImagen.setImageResource(R.mipmap.ic_launcher_round);
                    descripcion.setText("");
                }else{
                    Toast.makeText(getApplicationContext(), "NO HAY NINGUNA FOTOGRAFIA TOMADA", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button btnImagenes = (Button) findViewById(R.id.btnImagenes);

        btnImagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityLista.class);
                startActivity(intent);
            }
        });
    }

    private void permisos() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, PETICION_ACCESO_CAM);
        }else{
            tomarFoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PETICION_ACCESO_CAM){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                tomarFoto();
            }
        }else{
            Toast.makeText(getApplicationContext(), "SE NECESITAN PERMISOS DE ACCESO", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            getBytes(data);
        }
    }

    private void getBytes(Intent data){
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        objImagen.setImageBitmap(photo);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byteArray = stream.toByteArray();
    }

    private void tomarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

}