package com.homework.pm1e1201910080047.procesos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class TransaccionesContactos {
    public static final String DB_NAME = "mp1";
    public static final String TABLE = "e1";

    public static final String  ID = "id";
    public static final String PAIS = "pais";
    public static final String NUMERO = "numero";
    public static final String NOMBRE = "nombre";
    public static final String NOTA = "nota";
    public static final String FOTO = "foto";


    public static String obtenerSqlCrearTabla() {
        return "CREATE TABLE " + TABLE + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PAIS + " TEXT," +
                NUMERO + " INT," +
                NOMBRE + " TEXT," +
                NOTA + " TEXT," +
                FOTO + " BLOB)";
    }

    public static String obtenerSqlEliminarTabla() {
        return "DROP TABLE IF EXISTS " + TABLE;
    }

    public static void crearContacto(Context ctx, ContentValues valores) {
        try {
            Conexion cnn = new Conexion(ctx, DB_NAME, null, 1);
            SQLiteDatabase db = cnn.getWritableDatabase();
            Long resultado = db.insert(TABLE, ID, valores);
            db.close();
            Toast.makeText(ctx, "CONTACTO CREADO", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(ctx,"ha ocurrido un inconveniente!!",Toast.LENGTH_LONG).show();
        }
    }

    public static ArrayList<Contacto> obtenerListaContactos(Context ctx) {
        ArrayList<Contacto> Contactos = new ArrayList<>();

        try {
            Conexion cnn = new Conexion(ctx, DB_NAME, null, 1);
            SQLiteDatabase db = cnn.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT " + ID + ", " + PAIS + ", " + NUMERO + ", " + NOMBRE + ", " + NOTA + " FROM " + TABLE, null);

            Contacto contacto = null;
            while(c.moveToNext()) {
                contacto = new Contacto();
                contacto.setId(c.getInt(0));
                contacto.setPais(c.getString(1));
                contacto.setNumero(c.getInt(2));
                contacto.setNombre(c.getString(3));
                contacto.setNota(c.getString(4));

                Contactos.add(contacto);
            }

            c.close();
        } catch (Exception ex) {
            Toast.makeText(ctx,"ha ocurrido un inconveniente!!",Toast.LENGTH_LONG).show();
        }

        return Contactos;
    }

    public static ArrayList<String> obtenerArregloListaContactos(Context ctx) {
        ArrayList<String> listaContactos = new ArrayList<>();
        ArrayList<Contacto> contactos = obtenerListaContactos(ctx);

        for(int i=0; i<contactos.size(); i++) {
            listaContactos.add(contactos.get(i).getId().toString() + " - " + contactos.get(i).getNombre() + " " + contactos.get(i).getCodigoPais() + " " + contactos.get(i).getNumero().toString());
        }

        return listaContactos;
    }

    public static Bitmap obtenerFoto(Context ctx, String id) {
        Bitmap b = null;
        try {
            Conexion cnn = new Conexion(ctx, DB_NAME, null, 1);
            SQLiteDatabase db = cnn.getReadableDatabase();

            String[] params = {id};
            String[] fields = {FOTO};
            String filter = ID + "=?";

            Cursor c = db.query(TABLE, fields, filter, params, null, null, null);

            if (c.getCount() > 0) {
                c.moveToFirst();
                byte[] blob = c.getBlob(0);

                if (blob != null) {
                    ByteArrayInputStream bais = new ByteArrayInputStream(blob);
                    b = BitmapFactory.decodeStream(bais);
                }
            }

            db.close();
        } catch (Exception ex) {
            Toast.makeText(ctx,"ha ocurrido un inconveniente!!",Toast.LENGTH_LONG).show();
        }

        return b;
    }

    public static Contacto obtenerContacto(Context ctx, String id) {
        Contacto contacto = null;
        try {
            Conexion cnn = new Conexion(ctx, DB_NAME, null, 1);
            SQLiteDatabase db = cnn.getReadableDatabase();

            String[] params = {id};
            String[] fields = {ID,PAIS,NUMERO,NOMBRE,NOTA,FOTO};
            String filter = ID + "=?";

            Cursor c = db.query(TABLE, fields, filter, params, null, null, null);

            if (c.getCount() > 0) {
                c.moveToFirst();
                contacto = new Contacto();
                contacto.setId(c.getInt(0));
                contacto.setPais(c.getString(1));
                contacto.setNumero(c.getInt(2));
                contacto.setNombre(c.getString(3));
                contacto.setNota(c.getString(4));
                contacto.setFoto(c.getBlob(5));
            }

            db.close();
        } catch (Exception ex) {
            Toast.makeText(ctx,"ha ocurrido un inconveniente!!",Toast.LENGTH_LONG).show();
        }

        return contacto;
    }

    public static void actualizarContacto(Context ctx, ContentValues valores, String[] params) {
        try {
            Conexion cnn = new Conexion(ctx, DB_NAME, null, 1);
            SQLiteDatabase db = cnn.getWritableDatabase();
            int resultado = db.update(TABLE, valores, ID + "=?", params);
            db.close();
            Toast.makeText(ctx, "CONTACTO ACTUALIZADO", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(ctx,"ha ocurrido un inconveniente!!",Toast.LENGTH_LONG).show();
        }
    }

    public static void eliminarContancto(Context ctx, String[] params) {
        try {
            Conexion cnn = new Conexion(ctx, DB_NAME, null, 1);
            SQLiteDatabase db = cnn.getWritableDatabase();
            Integer resultado = db.delete(TABLE, "id=?", params);
            db.close();
            Toast.makeText(ctx, "CONTACTO ELIMINADO", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(ctx,"ha ocurrido un inconveniente!!",Toast.LENGTH_LONG).show();
        }
    }
}
