package com.loteria20.app;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Eduardo on 28-05-14.
 */

public class APILoteria extends AsyncTask<String, Integer, Boleto> {

    @Override
    protected Boleto doInBackground(String... params) {
        // TODO Auto-generated method stub
        return new Boleto();
        //return getData(params[0]);
    }

    protected void onPostExecute(String result) {
        /*String[] splitten = result.substring(1, result.length()-2).split("&");
        Hashtable<String, String> hasheado = new Hashtable<String, String>();
        for (String s: splitten){
            String[] a = s.split("=");
            hasheado.put(a[0], a[1]);
        }
        if(hasheado.get("respuesta").equals("1")){
            System.out.println("hay resultados");
            System.out.println("Los numeros son:");
            for (String s: hasheado.get("BolKino1").split(",")){
                System.out.println(s);
            }
        }else{
            System.out.println("No hay resultados aun");
        }
    }

    protected void onProgressUpdate(Integer... progress) {
*/
    }


    public String getData(String ticketNumber) {

        try{
            URL url = new URL("http://www.loteria.cl/KinoASP/procesa_consulta_kinoP3V2i016CJNK.asp?onHTTPStatus=%5Btype%20Function%5D&Nconsulta=1&panel=0&DV=&Rut=&boleto=0" + ticketNumber + "&sorteo=0");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder out = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                }

                reader.close();
                return out.toString();

            }
            catch(Exception e)
            {
                return "Error interno "+e.toString();
            }
            finally {
                urlConnection.disconnect();
            }
        }catch(Exception e){
            return "Error de conexión "+e.toString();

        }
    }
}
