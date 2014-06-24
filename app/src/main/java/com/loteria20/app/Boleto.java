package com.loteria20.app;

import java.util.Hashtable;

public class Boleto
{
    private int[] numeros = new int[14];
    private boolean[] coincidenciasChanchito = new boolean[14];
    private boolean[] coincidenciasKinoNormal = new boolean[14];
    private boolean[] coincidenciasReKino = new boolean[14];
    private int[] montos = new int[6];
    private boolean hayResultados = false;
    private String fechaSorteo = "";
    public String respuestaOriginal;
    private String respuestaOut;
    public String respuesta(){return respuestaOut;}




    public Boleto(String result)
    {
        respuestaOriginal = result;
        System.out.print(respuestaOriginal);
        if(result.length() <= 3 || !result.contains("&"))
        {
            respuestaOut = "Error de conección";
            hayResultados = false;
            return;
        }
        String[] splitten = result.substring(1, result.length()-2).split("&");
        Hashtable<String, String> ht = new Hashtable<String, String>();
        for (String s: splitten){
            String[] a = s.split("=");
            if(a.length>1)
                ht.put(a[0], a[1]);
        }

        if(ht.get("respuesta").equals("1")){
            hayResultados = true;

            fechaSorteo = ht.get("Fecha");

            montos[0] = Integer.parseInt(ht.get("Monto1"));
            montos[1] = Integer.parseInt(ht.get("MontoCJ1"));
            montos[2] = Integer.parseInt(ht.get("MontoCJ2P1"));
            montos[3] = Integer.parseInt(ht.get("MontoCon1"));
            montos[4] = Integer.parseInt(ht.get("MontoGM1"));
            montos[5] = Integer.parseInt(ht.get("MontoJuego1P1"));

            String[] stringNumbers = ht.get("BolKino1").split(",");
            numeros = new int[stringNumbers.length];
            int index = 0;
            for (String s : stringNumbers){
                numeros[index] = Integer.parseInt(s);
                index++;
            }

            String[] stringCoincidenciasChanchito = ht.get("AKinoJuego1P1").split(",");
            coincidenciasChanchito = stringTobooleanArray(stringCoincidenciasChanchito);
            String[] stringCoincidenciasKino = ht.get("AKino1").split(",");
            coincidenciasKinoNormal = stringTobooleanArray(stringCoincidenciasKino);
            String[] stringCoincidenciasReKino = ht.get("AReKino1").split(",");
            coincidenciasReKino = stringTobooleanArray(stringCoincidenciasReKino);

        }
        else if(ht.get("respuesta").equals("4"))
        {
            respuestaOut = "Código del billete ingresado no es válido.";
        }
        else if(ht.get("respuesta").equals("2"))
        {
            if(ht.get("mensaje").contains("prescrito"))
                respuestaOut = "Sorteo se realizó hace más de 60 días.";
            else
                respuestaOut = "Sorteo aún no se ha realizado.";
        }
        else
        {
            respuestaOut = "Error: " + respuestaOriginal;
        }

    }

    public boolean kino(){return true;}
    public boolean reKino(){return true;}
    public boolean chanchito(){return true;}

    public int[] getNumeros()
    {
        return numeros;
    }

    public String getFecha()
    {return fechaSorteo;}

    public boolean sorteoRealizado()
    {return hayResultados;}

    public int chanchitoRegalonNumeroAciertos()
    {return coincidenciasChanchito.length;}

    public int KinoNumeroAciertos()
    {return coincidenciasKinoNormal.length;}

    public int ReKinoNumeroAciertos()
    {return coincidenciasReKino.length;}

    public int[] listaChanchito(){
        int numberOfMatches = 0;
        for(boolean b : coincidenciasChanchito){
            if (b) numberOfMatches++;
        }
        int[] numerosCoincidentes = new int[numberOfMatches];
        int indexCoincidentes = 0;
        for(int i = 0; i< coincidenciasChanchito.length; i++){
            if(coincidenciasChanchito[i]){
                numerosCoincidentes[indexCoincidentes] = numeros[i];
                indexCoincidentes++;
            }
        }
        return numerosCoincidentes;
    }

    public int[] listaKino(){
        int numberOfMatches = 0;
        for(boolean b : coincidenciasKinoNormal){
            if (b) numberOfMatches++;
        }
        int[] numerosCoincidentes = new int[numberOfMatches];
        int indexCoincidentes = 0;
        for(int i = 0; i< coincidenciasKinoNormal.length; i++){
            if(coincidenciasKinoNormal[i]){
                numerosCoincidentes[indexCoincidentes] = numeros[i];
                indexCoincidentes++;
            }
        }
        return numerosCoincidentes;
    }

    public int[] listaReKino(){
        int numberOfMatches = 0;
        for(boolean b : coincidenciasReKino){
            if (b) numberOfMatches++;
        }
        int[] numerosCoincidentes = new int[numberOfMatches];
        int indexCoincidentes = 0;
        for(int i = 0; i< coincidenciasReKino.length; i++){
            if(coincidenciasReKino[i]){
                numerosCoincidentes[indexCoincidentes] = numeros[i];
                indexCoincidentes++;
            }
        }
        return numerosCoincidentes;
    }

    public int[] premios()
    {
        return montos;
    }

    private boolean[] stringTobooleanArray(String[] values){
        boolean[] result = new boolean[values.length];
        for(int i = 0; i < values.length; i++){
            if(values[i].equals("01")){
                result[i] = true;
            }
        }
        return result;
    }

}
