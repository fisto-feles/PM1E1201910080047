package com.homework.pm1e1201910080047;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.homework.pm1e1201910080047.procesos.Contacto;
import com.homework.pm1e1201910080047.procesos.TransaccionesContactos;

import java.util.ArrayList;

public class ListaContactos extends AppCompatActivity {

    ListView lvContactos;
    TextView tvItemSeleccionado;
    Button btnCompartir, btnEliminar, btnActualizar, btnVer, btnRegresar, btnRefrescar, btnLlamar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contactos);

        obtenerElementosLayout();

        ArrayAdapter<String> adapter = new ArrayAdapter<>( ListaContactos.this, android.R.layout.simple_list_item_1, TransaccionesContactos.obtenerArregloListaContactos(ListaContactos.this));
        lvContactos.setAdapter(adapter);

        lvContactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                tvItemSeleccionado.setText(lvContactos.getItemAtPosition(position).toString());
            }
        });


        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvItemSeleccionado.getText().toString().isEmpty()) {
                    Toast.makeText(ListaContactos.this, "Debe seleccionar un contacto!!", Toast.LENGTH_SHORT).show();
                    return;
                }


                String id = tvItemSeleccionado.getText().toString().substring(0, tvItemSeleccionado.getText().toString().indexOf('-'));
                String[] params = {id};

                TransaccionesContactos.eliminarContancto(ListaContactos.this, params);
                refrescarLista();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvItemSeleccionado.getText().toString().isEmpty()) {
                    Toast.makeText(ListaContactos.this, "Debe seleccionar un contacto!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent i = new Intent(getApplicationContext(), ActualizarContacto.class);
                i.putExtra("contacto", tvItemSeleccionado.getText().toString());
                startActivity(i);
            }
        });

        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvItemSeleccionado.getText().toString().isEmpty()) {
                    Toast.makeText(ListaContactos.this, "Debe seleccionar un contacto!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent i = new Intent(getApplicationContext(), VerFoto.class);
                i.putExtra("contacto", tvItemSeleccionado.getText().toString());
                startActivity(i);
            }
        });

        btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvItemSeleccionado.getText().toString().isEmpty()) {
                    Toast.makeText(ListaContactos.this, "Debe seleccionar un contacto!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String id = tvItemSeleccionado.getText().toString().substring(0, tvItemSeleccionado.getText().toString().indexOf('-'));
                String[] params = {id};
                Contacto contacto = TransaccionesContactos.obtenerContacto(ListaContactos.this, id);

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "+" + contacto.getCodigoPais().toString() + contacto.getNumero().toString());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

            }
        });

        btnLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvItemSeleccionado.getText().toString().isEmpty()) {
                    Toast.makeText(ListaContactos.this, "Debe seleccionar un contacto!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String id = tvItemSeleccionado.getText().toString().substring(0, tvItemSeleccionado.getText().toString().indexOf('-'));
                String[] params = {id};
                Contacto contacto = TransaccionesContactos.obtenerContacto(ListaContactos.this, id);

                if(ActivityCompat.checkSelfPermission(ListaContactos.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Llamando", Toast.LENGTH_LONG).show();
                    Intent llamada = new Intent(Intent.ACTION_CALL);
                    llamada.setData(Uri.parse("tel: +" + contacto.getCodigoPais().toString() + contacto.getNumero().toString()));
                    startActivity(llamada);
                }else{
                    ActivityCompat.requestPermissions(ListaContactos.this, new String[]{ Manifest.permission.CALL_PHONE}, 1);
                }
            }
        });

        btnRefrescar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refrescarLista();
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void refrescarLista() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>( ListaContactos.this, android.R.layout.simple_list_item_1, TransaccionesContactos.obtenerArregloListaContactos(ListaContactos.this));
        lvContactos.setAdapter(adapter);
    }

    private void obtenerElementosLayout() {
        lvContactos = (ListView) findViewById(R.id.lvContactos);
        tvItemSeleccionado = (TextView) findViewById(R.id.tvItemSeleccionado);
        btnActualizar = (Button) findViewById(R.id.btnActualizar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        btnCompartir = (Button) findViewById(R.id.btnCompartir);
        btnVer = (Button) findViewById(R.id.btnVer);
        btnRegresar = (Button) findViewById(R.id.btnRegresar);
        btnRefrescar = (Button) findViewById(R.id.btnRefrescar);
        btnLlamar = (Button) findViewById(R.id.btnLlamar);
    }
}
