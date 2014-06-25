package com.loteria20.app;

/**
 * Created by felipe on 25-06-14.
 */
public class Billete {

    private String _nombre, _codigo, _tipo, _respuesta, _estado;
    private int _id;


    public Billete (int id, String nombre, String codigo, String tipo, String respuesta, String _estado){
        _id=id;
        _nombre=nombre;
        _codigo=codigo;
        _tipo=tipo;
        _respuesta=respuesta;
        _estado=_estado;
    }

    public int getId(){return _id;}

    public String getNombre(){return _nombre;}

    public String getCodigo(){return _codigo;}

    public String getTipo(){return _tipo;}

    public String getRespuesta(){return _respuesta;}

    public String getEstado(){return _estado;}
}
