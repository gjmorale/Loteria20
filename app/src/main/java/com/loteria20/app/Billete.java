package com.loteria20.app;

/**
 * Created by felipe on 25-06-14.
 */
public class Billete {

    private String _nombre, _codigo, _tipo, _respuesta;
    private int _id, _estado;


    public Billete (int id, String nombre, String codigo, String tipo, String respuesta, int estado){
        _id=id;
        _nombre=nombre;
        _codigo=codigo;
        _tipo=tipo;
        _respuesta=respuesta;
        _estado=estado;
    }

    public int getId(){return _id;}

    public String getNombre(){return _nombre;}

    public String getCodigo(){return _codigo;}

    public String getTipo(){return _tipo;}

    public String getRespuesta(){return _respuesta;}

    public int getEstado(){return _estado;}

    public void setEstado(int estado){_estado=estado;}

    public void setRespuesta(String respuesta){_respuesta=respuesta;}

    public void setNombre(String nombre){_nombre=nombre;}
}
