package com.loteria20.app;

/**
 * Created by felipe on 25-06-14.
 */
public class ElementoSpinner {

    private int _id;
    private String _nombre;

    public ElementoSpinner (int id, String nombre){
        _id=id;
        _nombre=nombre;
    }

    public int getId(){return _id;}
    public String getNombre(){return _nombre;}

    public String toString(){ return _nombre;}
}
