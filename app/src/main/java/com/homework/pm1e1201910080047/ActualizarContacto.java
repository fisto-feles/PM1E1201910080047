package com.homework.pm1e1201910080047;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.homework.pm1e1201910080047.procesos.Contacto;
import com.homework.pm1e1201910080047.procesos.TransaccionesContactos;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActualizarContacto extends AppCompatActivity {

    public static final int PETICION_CAMARA = 100;
    public static final int PETICION_TOMAR_FOTO = 101;

    String rutaFoto;
    String rutaFotoActual;
    String id;

    ImageView ivActualizarFoto;
    EditText etActualizarNombre, etActualizarNumero, etActualizarNota;
    Button btnActualizarSalvar, btnActualizarContactos, btnActualizarFoto;
    Spinner spnActualizarPais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_contacto);

        obtenerElementosLayout();

        ArrayList<String> paises = new ArrayList<>();
        paises.add("Honduras (504)");
        paises.add("Guatemala (502)");
        paises.add("Salvador (503)");
        paises.add("Costa Rica (506)");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ActualizarContacto.this, android.R.layout.simple_spinner_dropdown_item, paises);
        spnActualizarPais.setAdapter(adapter);

        String data = getIntent().getExtras().getString("contacto");
        id = data.substring(0, data.indexOf('-'));

        Contacto contacto = TransaccionesContactos.obtenerContacto(ActualizarContacto.this, id);

        if (contacto != null) {
            int position = 0;
            switch (contacto.getPais()) {
                case "Honuras (504)":
                    position = 0;
                    break;
                case "Guatemala (502)":
                    position = 1;
                    break;
                case "Salvador (503)":
                    position = 2;
                    break;
                case "Costa Rica (506)":
                    position = 3;
                    break;
            }

            spnActualizarPais.setSelection(position);
            etActualizarNumero.setText(contacto.getNumero().toString());
            etActualizarNombre.setText(contacto.getNombre());
            etActualizarNota.setText(contacto.getNota());
            ivActualizarFoto.setImageBitmap(contacto.getFoto());
        }

        btnActualizarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otorgarPermisos();
            }
        });

        btnActualizarSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etActualizarNumero.getText().toString().isEmpty()) {
                    Toast.makeText(ActualizarContacto.this, "Debe ingresar un telefono", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (etActualizarNombre.getText().toString().isEmpty()) {
                    Toast.makeText(ActualizarContacto.this, "Debe ingresar un nombre", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (etActualizarNota.getText().toString().trim().isEmpty()) {
                    Toast.makeText(ActualizarContacto.this, "Debe ingresar una nota", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues valores = new ContentValues();
                valores.put(TransaccionesContactos.PAIS, spnActualizarPais.getSelectedItem().toString());
                valores.put(TransaccionesContactos.NUMERO, etActualizarNumero.getText().toString());
                valores.put(TransaccionesContactos.NOMBRE, etActualizarNombre.getText().toString());
                valores.put(TransaccionesContactos.NOTA, etActualizarNota.getText().toString());

                byte[] blob = null;

                try {
                    Bitmap bmo = ((BitmapDrawable) ivActualizarFoto.getDrawable()).getBitmap();
                    Bitmap bms = Bitmap.createScaledBitmap(bmo, bmo.getWidth() / (bmo.getHeight() / 500),500, false);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream(20480);
                    bms.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    blob = baos.toByteArray();
                } catch (Exception ex) {

                }

                valores.put(TransaccionesContactos.FOTO, blob);

                TransaccionesContactos.actualizarContacto(ActualizarContacto.this, valores, new String[] {id});
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int codigoPeticion, @NonNull String[] permisos, @NonNull int[] resultadoOtogacion) {
        super.onRequestPermissionsResult(codigoPeticion, permisos, resultadoOtogacion);

        if (codigoPeticion != PETICION_CAMARA) {
            return;
        }

        if (resultadoOtogacion.length > 0 && resultadoOtogacion[0] == PackageManager.PERMISSION_GRANTED) {
            ejecutarTomarFotoIntent();
            return;
        }

        Toast.makeText(getApplicationContext(), "Permisos denegados", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int codigoPeticion, int codigoResultado, @NonNull Intent datos) {
        super.onActivityResult(codigoPeticion, codigoResultado, datos);

        if (codigoPeticion == PETICION_TOMAR_FOTO && codigoResultado == RESULT_OK) {
            File foto = new File(rutaFotoActual);
            ivActualizarFoto.setImageURI(Uri.fromFile(foto));
            // agregarFotoGaleria();
        }
    }

    private void agregarFotoGaleria() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File imagen = new File(rutaFotoActual);
        Uri uriContenido = Uri.fromFile(imagen);
        mediaScanIntent.setData(uriContenido);
        this.sendBroadcast(mediaScanIntent);
    }

    private void otorgarPermisos() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActualizarContacto.this, new String[] { Manifest.permission.CAMERA }, PETICION_CAMARA);
            return;
        }

        ejecutarTomarFotoIntent();
    }

    private void ejecutarTomarFotoIntent() {
        Intent tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (tomarFotoIntent.resolveActivity(getPackageManager()) == null) {
            return;
        }

        File foto = null;
        try {
            foto = crearArchivoImagen();
        } catch (Exception ex) {

        }

        if (foto != null) {
            Uri uriFoto = FileProvider.getUriForFile(ActualizarContacto.this, "com.homework.pm1e1201910080047.fileprovider", foto);
            tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto);
            startActivityForResult(tomarFotoIntent, PETICION_TOMAR_FOTO);
        }
    }

    private File crearArchivoImagen() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombreArchivo = "IMG_" + timeStamp + "_";
        File directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombreArchivo, ".jpg", directorio);
        rutaFotoActual = imagen.getAbsolutePath();
        return imagen;
    }

    private void obtenerElementosLayout() {
        ivActualizarFoto = (ImageView) findViewById(R.id.ivActualizarFoto);
        spnActualizarPais = (Spinner) findViewById(R.id.spnPaisActualizar);
        etActualizarNumero = (EditText) findViewById(R.id.etNumeroActualizar);
        etActualizarNombre = (EditText) findViewById(R.id.etNombreActualizar);
        etActualizarNota = (EditText) findViewById(R.id.etNotaActualizar);
        btnActualizarSalvar = (Button) findViewById(R.id.btnGuardarActualizacion);
        btnActualizarFoto = (Button) findViewById(R.id.btnFotoActualizar);
    }
}