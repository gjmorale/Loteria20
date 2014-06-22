package com.loteria20.app;

/**
 * Created by gmo on 6/20/14.
 */


public class Boleto
{
    public enum juegos {KINO, REKINO, CHANCHITO;}


    //en numeros almacenamos los numeros del carton
    private int[] numeros;

    //indicamos de que juego es este carton
    private juegos tipoJuego;

    String Fecha;
    boolean realizado;

    public Boleto()
    {

    }

    public boolean kino()
    {
        if(tipoJuego==juegos.KINO)
            return true;
        else
            return false;
    }

    public boolean reKino()
    {
        if(tipoJuego==juegos.REKINO)
            return true;
        else
            return false;
    }

    public boolean chanchito()
    {
        if(tipoJuego==juegos.CHANCHITO)
            return true;
        else
            return false;
    }

    public int[] getNumeros()
    {
       return new int[]{1,2,3,4,5};
       //return numeros;
    }

    public String getFecha()
    {
        return "Hoy día";
        //return Fecha;
    }

    public boolean sorteoRealizado()
    {
        return true;
        //return realizado;
    }

    public int chanchitoRegalonNumeroAciertos()
    {return 5;}

    public int KinoNumeroAciertos()
    {return 5;}

    public int ReKinoNumeroAciertos()
    {return 5;}

    public int[] listaChanchito()
    {return new int[]{1,3};}

    public int[] listaKino()
    {return new int[]{1,2};}

    public int[] listaReKino()
    {return new int[]{2,3};}

    public int[] premios()
    {return new int[]{100,1000,2000};}

}
