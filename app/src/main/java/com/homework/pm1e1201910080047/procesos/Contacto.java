package com.homework.pm1e1201910080047.procesos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;

public class Contacto {
    private Integer id;
    private String pais;
    private Integer numero;
    private String nombre;
    private String nota;
    private byte[] foto;

    public Contacto() {}
    public Contacto(Integer id, String pais, Integer numero, String nombre, String nota, byte[] foto) {
        this.id = id;
        this.pais = pais;
        this.numero = numero;
        this.nombre = nombre;
        this.nota = nota;
        this.foto = foto;
    }

    public Integer getId() { return this.id; }
    public void setId(Integer id) { this.id = id; }

    public String getPais() { return this.pais; }
    public Integer getCodigoPais() {

        Integer codigo = null;

        switch (this.pais) {
            case "Honduras (504)":
                codigo = 504;
                break;
            case "Guatemala (502)":
                codigo = 502;
                break;
            case "Salvador (503)":
                codigo = 503;
                break;
            case "Costa Rica (506)":
                codigo = 506;
                break;
        }

        return codigo;
    }
    public void setPais(String pais) { this.pais = pais; }

    public Integer getNumero() { return this.numero; }
    public void setNumero(Integer numero) { this.numero = numero; }

    public String getNombre() { return this.nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNota() { return this.nota; }
    public void setNota(String nota) { this.nota = nota; }

    public Bitmap getFoto() {
        if (this.foto == null) {
            return null;
        }
        
        ByteArrayInputStream bais = new ByteArrayInputStream(this.foto);
        return BitmapFactory.decodeStream(bais);
    }
    public void setFoto(byte[] foto) { this.foto = foto; }
}
