package com.loteria20.app;

import java.util.Hashtable;

public class Boleto
{
    private int[] numeros = new int[14];
    private boolean[] coincidenciasChanchito = new boolean[14];
    private boolean[] coincidenciasKinoNormal = new boolean[14];
    private boolean[] coincidenciasReKino = new boolean[14];
    private boolean[] coincidenciasGanaMas = new boolean[14];
    private boolean[][] coincidenciasChaoJefe = new boolean[4][];
    private boolean[][] coincidenciasComboMarraqueta = new boolean[5][];
    public boolean kino = false;
    public boolean reKino = false;
    public boolean chanchito = false;
    public boolean ganaMas = false;
    public boolean chaoJefe = false;
    public boolean comboMarraqueta = false;
    private int[] montos = new int[6];
    private boolean hayResultados = false;
    private String fechaSorteo = "";
    public String respuestaOriginal;
    private String respuestaOut;
    public String respuesta(){return respuestaOut;}




    public Boleto(String result)
    {
        respuestaOriginal = result;
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
            kino = ht.get("AKino1").length() > 0 ;
            reKino = ht.get("AReKino1").length() > 0 ;
            chanchito = ht.get("AKinoJuego1P1").length() > 0 ;
            ganaMas = ht.get("AKinoMG1").length() > 0 ;
            chaoJefe = ht.get("AKinoCJOp11").length() > 0;
            comboMarraqueta = ht.get("AKinoCJ2Op11").length() > 0;
            if(chanchito){
            String[] stringCoincidenciasChanchito = ht.get("AKinoJuego1P1").split(",");
            coincidenciasChanchito = stringTobooleanArray(stringCoincidenciasChanchito);
            }
            if(kino){
            String[] stringCoincidenciasKino = ht.get("AKino1").split(",");
            coincidenciasKinoNormal = stringTobooleanArray(stringCoincidenciasKino);
            }
            if(reKino){
            String[] stringCoincidenciasReKino = ht.get("AReKino1").split(",");
            coincidenciasReKino = stringTobooleanArray(stringCoincidenciasReKino);
            }
            if(ganaMas){
            String[] stringCoincidenciasGanaMas = ht.get("AKinoMG1").split(",");
            coincidenciasGanaMas = stringTobooleanArray(stringCoincidenciasGanaMas);
            }
            if(chaoJefe){
                coincidenciasChaoJefe[0] = stringTobooleanArray(ht.get("AKinoCJOp11").split(","));
                coincidenciasChaoJefe[1] = stringTobooleanArray(ht.get("AKinoCJOp21").split(","));
                coincidenciasChaoJefe[2] = stringTobooleanArray(ht.get("AKinoCJOp31").split(","));
                coincidenciasChaoJefe[3] = stringTobooleanArray(ht.get("AKinoCJOp41").split(","));
            }
            if(comboMarraqueta){
                coincidenciasComboMarraqueta[0] = stringTobooleanArray(ht.get("AKinoCJ2Op11").split(","));
                coincidenciasComboMarraqueta[1] = stringTobooleanArray(ht.get("AKinoCJ2Op21").split(","));
                coincidenciasComboMarraqueta[2] = stringTobooleanArray(ht.get("AKinoCJ2Op31").split(","));
                coincidenciasComboMarraqueta[3] = stringTobooleanArray(ht.get("AKinoCJ2Op41").split(","));
                coincidenciasComboMarraqueta[4] = stringTobooleanArray(ht.get("AKinoCJ2Op41").split(","));
            }


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


    public int[] getNumeros(){
        return numeros;
    }

    public String getFecha()
    {return fechaSorteo;}

    public boolean sorteoRealizado()
    {return hayResultados;}

    public int chanchitoRegalonNumeroAciertos()
    {return coincidenciasChanchito.length;}

    public int kinoNumeroAciertos()
    {return coincidenciasKinoNormal.length;}

    public int reKinoNumeroAciertos()
    {return coincidenciasReKino.length;}

    public int ganaMasNumeroAciertos()
    {return coincidenciasGanaMas.length;}

    public int[] listaChanchito(){
        return getMatches(coincidenciasChanchito);
    }

    public int[] listaKino(){
        return getMatches(coincidenciasKinoNormal);
    }

    public int[] listaReKino(){
        return getMatches(coincidenciasReKino);
    }

    public int[] listaGanaMas(){
        return getMatches(coincidenciasGanaMas);
    }

    public int[][] listasChaoJefe(){
        int[][] result = new int[4][];
        result[0] = getMatches(coincidenciasChaoJefe[0]);
        result[1] = getMatches(coincidenciasChaoJefe[1]);
        result[2] = getMatches(coincidenciasChaoJefe[2]);
        result[3] = getMatches(coincidenciasChaoJefe[3]);
        return result;
    }

    public int[][] listasComboMarraqueta(){
        int[][] result = new int[5][];
        result[0] = getMatches(coincidenciasComboMarraqueta[0]);
        result[1] = getMatches(coincidenciasComboMarraqueta[1]);
        result[2] = getMatches(coincidenciasComboMarraqueta[2]);
        result[3] = getMatches(coincidenciasComboMarraqueta[3]);
        result[4] = getMatches(coincidenciasComboMarraqueta[4]);
        return result;
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

    private int[] getMatches(boolean[] matches){
        int numberOfMatches = 0;
        for(boolean b : matches){
            if (b) numberOfMatches++;
        }
        int[] numerosCoincidentes = new int[numberOfMatches];
        int indexCoincidentes = 0;
        for(int i = 0; i< matches.length; i++){
            if(matches[i]){
                numerosCoincidentes[indexCoincidentes] = numeros[i];
                indexCoincidentes++;
            }
        }
        return numerosCoincidentes;
    }
}
