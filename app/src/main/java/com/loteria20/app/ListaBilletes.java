package com.loteria20.app;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class ListaBilletes extends ActionBarActivity {

    private boolean vacio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_billetes);

        List<String> defecto = new ArrayList<String>();
        defecto.add("Aún no tiene billetes registrados");
        setUpSpinner(defecto);

        vacio = true;

        Spinner sp = (Spinner)findViewById(R.id.lista_billetes);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(!vacio)
                    showResult(position);
                else
                    showResult(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {}

        });

        Controlador_Lista.start();

        showResult(0);
    }

    private void setUpSpinner(List<String> list)
    {

        /*Preparar spinner con opciones de distintos juegos*/
        Spinner spinner1 = (Spinner) findViewById(R.id.lista_billetes);

        if(list.size()==0)
        {
            list.add("Aún no tiene billetes registrados");
            vacio = true;
        }
        else
            vacio = false;

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, R.layout.my_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(dataAdapter);

    }

    private void showResult(int posicion)
    {
        FragmentoResultado res = new FragmentoResultado();
        if(!vacio)
            res.setPosicion(posicion);
        else
            res.setPosicion(-1);
        FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.container,(Fragment)res);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lista_billetes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_capture) {
            Intent intent = new Intent(this, Captura_Billete.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_agregar) {
            Intent intent = new Intent(this, Captura_Billete.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_eliminar && !vacio) {
            confirmarEliminar(((Spinner) findViewById(R.id.lista_billetes)).getSelectedItemPosition());
            return true;
        }

        if (id == R.id.action_renombrar && !vacio) {
            inputRenombrar(((Spinner) findViewById(R.id.lista_billetes)).getSelectedItemPosition());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void inputRenombrar(final int index)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(Controlador_Lista.getNombre(index));
        alert.setMessage("Ingrese nuevo nombre:");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                String value = input.getText().toString();
                Controlador_Lista.renombrar(index, value);
                setUpSpinner(Controlador_Lista.getNombres());
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                // Canceled.
            }
        });

        alert.show();
    }

    private void confirmarEliminar(final int index)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(Controlador_Lista.getNombre(index));
        alert.setMessage("¿Seguro que quiere eliminar este billete?");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                Controlador_Lista.borrar(index);
                setUpSpinner(Controlador_Lista.getNombres());
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                // Canceled.
            }
        });

        alert.show();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        List<String> list = Controlador_Lista.getNombres();
        if(list.size()>0)
            setUpSpinner(list);

    }

}
