package com.homework.pm1e1201910080047;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.homework.pm1e1201910080047.procesos.TransaccionesContactos;

public class VerFoto extends AppCompatActivity {

    ImageView ivVerFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_foto);

        String contacto = getIntent().getExtras().getString("contacto");
        String id = contacto.substring(0, contacto.indexOf('-'));

        ivVerFoto = (ImageView) findViewById(R.id.ivVerFoto);
        ivVerFoto.setImageBitmap(TransaccionesContactos.obtenerFoto(VerFoto.this, id.trim()));
    }
}