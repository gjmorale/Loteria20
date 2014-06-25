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

public class APILoteria extends AsyncTask<String, Integer, String> {

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        return getData(params[0]);

    }

    protected void onPostExecute(String result) {
    }


    public String getData(String ticketNumber) {

        try{
            URL url = new URL("http://www.loteria.cl/KinoASP/procesa_consulta_kinoP3V2i016CJNK.asp?onHTTPStatus=%5Btype%20Function%5D&Nconsulta=1&panel=0&DV=&Rut=&boleto=" + ticketNumber + "&sorteo=0");
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
