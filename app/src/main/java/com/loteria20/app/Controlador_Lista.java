package com.loteria20.app;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Controlador_Lista
{
    private static File BD;
    private static String separador = "%#%";
    public static String getSeparador()
    { return separador; }
    private static String EOF = "%@%";
    public static String getEOF()
    { return EOF; }

    private static String separador2 = "-";
    public static String getSeparador2()
    { return separador2; }

    //Instancia de la base de datos
    static public DatabaseHandler dbHandler;

    //Inicializa el sistema de archivos.
    public static void start(Context context){

        //Instanciamos la BD
        dbHandler = new DatabaseHandler(context);

        /*
        boolean ret = false;
        File root = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/BD");
        if(!root.exists())
        {
            root.mkdir();
        }
        BD = new File(root, "BD.txt");
        try {
            FileOutputStream f = new FileOutputStream(BD,true);
            PrintWriter pw = new PrintWriter(f);
            pw.print("");
            pw.flush();
            pw.close();
            f.close();

        } catch (FileNotFoundException e) {
        } catch (IOException e) { }

        //Verificar que hay billetes en el archivo ademas de la opcionMinima
        if(getNombres().size() > 0)
            ret = true;
        return ret;
        */
    }

    //Retorna toda la informacion del billete. Importante que el indice parta de cero
    //Obsoleto
    /*
    private static String[] getInfoBillete(int posicion)
    {
        String [] arreglo = new String[1];
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(BD)));
            try{
                int i = 0;
                while(i < posicion)
                {
                    br.readLine();
                    i++;
                }
                arreglo = br.readLine().split(separador);
                br.close();
            } catch(IOException ioe) {}
        } catch (FileNotFoundException e) {}
        if(arreglo != null)
        {
            return arreglo;
        } else { return null; }
    }
    */

    public static String getRespuesta(int id)
    {
        return dbHandler.getBillete(id).getRespuesta();
//        return getInfoBillete(posicion)[3];
    }

    public static String getNombre(int id)
    {
        return dbHandler.getBillete(id).getNombre();
        //return getInfoBillete(posicion)[0];
    }

    public static int getEstado(int id)
    {
        return dbHandler.getBillete(id).getEstado();
//        return getInfoBillete(posicion)[2];
    }

    public static String getCodigo(int id)
    {
        return dbHandler.getBillete(id).getCodigo();
        //return getInfoBillete(posicion)[1];
    }

    //Retorna nombre de elementos a mostrar en la lista del navegador
    //opcionMinima es para asegurar que siempre habrá algo que mostrar en el drawer
    public static List<ElementoSpinner> getNombres()
    {
        //tratemos de implementarlo con bd

        //String line = "";
        List<ElementoSpinner> lines = new ArrayList<ElementoSpinner>();

        //Obtenemos los nombres de la BD
        List<Billete> todosLosBilletes = dbHandler.getAllBilletes();
        for(int i=0; i<todosLosBilletes.size();i++ )
        {
            int idBillete = todosLosBilletes.get(i).getId();
            String nombreBillete = todosLosBilletes.get(i).getNombre();
            lines.add(new ElementoSpinner(idBillete, nombreBillete));
            //lines.add(todosLosBilletes.get(i).getNombre());
        }

        return lines;

        /*
        String [] arreglo;
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(BD)));
            try{
                while((line = br.readLine()) != null)
                {
                    lines.add(line.split(separador)[0]);
                }
                br.close();
            } catch(IOException ioe) {}
        } catch (FileNotFoundException e) {}
        arreglo = lines.toArray(new String[lines.size()]);
        */

    }

    //Al parecer esto quedaria obsoleto, ya que ahora guardo en la BD
    /*
    public static boolean setBillete(String nombre, String codigo, String tipo, String resultado)
    {
        try {
            FileOutputStream f = new FileOutputStream(BD,true);
            PrintWriter pw = new PrintWriter(f);
            pw.println(nombre+separador+codigo+separador+tipo+separador+resultado);
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) { return false;
        } catch (IOException e) { return false;}

        return true;
    }
    */

    /*Renombre un billete*/
    public static void renombrar(int index, String nombre)
    {
        Billete billete = dbHandler.getBillete(index);
        billete.setNombre(nombre);
        dbHandler.updateBillete(billete);

        /*

        int i = 0;
        String line = "";
        List<String> lines = new ArrayList<String>();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(BD)));
            try{
                while((line = br.readLine()) != null)
                {
                    if(i==index)
                    {
                        String[] temp = line.split(separador);
                        temp[0] = nombre;
                        line = temp[0]+separador+temp[1]+separador+temp[2]+separador+temp[3];
                    }
                    lines.add(line);
                    //System.out.println(lines.get(lines.size()-1));
                    i++;
                }
                br.close();
            }
            catch(IOException ioe) { }
        }
        catch (FileNotFoundException e) { }

        try
        {
            FileOutputStream f = new FileOutputStream(BD,false);
            PrintWriter pw = new PrintWriter(f);
            for(int j = 0; j < lines.size() ; j++)
                pw.println(lines.get(j));
            pw.flush();
            pw.close();
            f.close();
        }
        catch (FileNotFoundException e) { }
        catch (IOException e) { }
        */
    }

    /*Igual a borrar pero reescribe el billete index con respuesta cambiado*/
    public static void setResultado(int index, String respuesta)
    {
        //Ahora con BD
        Billete billete = dbHandler.getBillete(index);
        billete.setRespuesta(respuesta);
        dbHandler.updateBillete(billete);



        /*
        int i = 0;
        String line = "";
        List<String> lines = new ArrayList<String>();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(BD)));
            try{
                while((line = br.readLine()) != null)
                {
                    if(i==index)
                    {
                        String[] temp = line.split(separador);
                        temp[3] = resultado;
                        line = temp[0]+separador+temp[1]+separador+temp[2]+separador+temp[3];
                    }
                    lines.add(line);
                    i++;
                }
                br.close();
            }
            catch(IOException ioe) { }
        }
        catch (FileNotFoundException e) { }

        try
        {
            FileOutputStream f = new FileOutputStream(BD,false);
            PrintWriter pw = new PrintWriter(f);
            for(int j = 0; j < lines.size() ; j++)
                pw.println(lines.get(j));
            pw.flush();
            pw.close();
            f.close();
        }
        catch (FileNotFoundException e) { }
        catch (IOException e) { }
        */
    }

    /*Igual a borrar pero reescribe el billete index con estado cambiado*/
    public static void setEstado(int index, int estado)
    {
        //Ahora con BD
        Billete billete = dbHandler.getBillete(index);
        billete.setEstado(estado);
        dbHandler.updateBillete(billete);

        /*
        int i = 0;
        String line = "";
        List<String> lines = new ArrayList<String>();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(BD)));
            try{
                while((line = br.readLine()) != null)
                {
                    if(i==index)
                    {
                        String[] temp = line.split(separador);
                        temp[2] = estado;
                        line = temp[0]+separador+temp[1]+separador+temp[2]+separador+temp[3];
                    }
                    lines.add(line);
                    i++;
                }
                br.close();
            }
            catch(IOException ioe) { }
        }
        catch (FileNotFoundException e) { }

        try
        {
            FileOutputStream f = new FileOutputStream(BD,false);
            PrintWriter pw = new PrintWriter(f);
            for(int j = 0; j < lines.size() ; j++)
                pw.println(lines.get(j));
            pw.flush();
            pw.close();
            f.close();
        }
        catch (FileNotFoundException e) { }
        catch (IOException e) { }
        */
    }

    public static void borrar(int index)
    {
        //ahora con BD
        dbHandler.deleteBillete(dbHandler.getBillete(index));


        //con archivos
        /*
        int i = 0;
        String line = "";
        List<String> lines = new ArrayList<String>();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(BD)));
            try{
                while((line = br.readLine()) != null)
                {
                    if(i!=index)
                        lines.add(line);
                    //System.out.println(lines.get(lines.size()-1));
                    i++;
                }
                br.close();
            }
            catch(IOException ioe) { }
        }
        catch (FileNotFoundException e) { }

        try
        {
            FileOutputStream f = new FileOutputStream(BD,false);
            PrintWriter pw = new PrintWriter(f);
            for(int j = 0; j < lines.size() ; j++)
                pw.println(lines.get(j));
            pw.flush();
            pw.close();
            f.close();
        }
        catch (FileNotFoundException e) { }
        catch (IOException e) { }

        */


    }

}
