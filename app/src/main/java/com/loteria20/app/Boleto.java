package com.loteria20.app;

/**
 * Created by gmo on 6/20/14.
 */
public class Boleto
{

    private int[] numeros;

    public Boleto()
    {

    }

    public boolean kino()
    {
        return true;
    }

    public boolean reKino()
    {
        return true;
    }

    public boolean chanchito()
    {
        return true;
    }

    public int[] getNumeros()
    {
        return new int[]{1,2,3,4,5};
    }

    public String getFecha()
    {return "Hoy día";}

    public boolean sorteoRealizado()
    {return true;}

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
