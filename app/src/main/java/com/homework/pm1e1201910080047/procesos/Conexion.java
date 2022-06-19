package com.homework.pm1e1201910080047.procesos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Conexion extends SQLiteOpenHelper {

    public Conexion(@Nullable Context ctx, @Nullable String dbName, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(ctx, dbName, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TransaccionesContactos.obtenerSqlCrearTabla());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(TransaccionesContactos.obtenerSqlEliminarTabla());
        onCreate(db);
    }
}
