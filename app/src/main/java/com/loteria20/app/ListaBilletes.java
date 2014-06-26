package com.loteria20.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
    private int mPos;
    private FragmentoResultado mFragmento;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_billetes);

        List<ElementoSpinner> defecto = new ArrayList<ElementoSpinner>();
        defecto.add(new ElementoSpinner(-1,"Aún no tiene billetes registrados"));
        setUpSpinner(defecto);

        vacio = true;
        mPos = 0;

        if(savedInstanceState != null && savedInstanceState.containsKey("posicion"))
            mPos = savedInstanceState.getInt("posicion");

        mFragmento = (FragmentoResultado)getFragmentManager().findFragmentById(R.id.fragmento);

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


        Controlador_Lista.start(getApplicationContext());
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putInt("posicion", mPos);

        super.onSaveInstanceState(savedInstanceState);
    }

    private void setUpSpinner(List<ElementoSpinner> list)
    {
        /*Preparar spinner con opciones de distintos juegos*/
        Spinner spinner1 = (Spinner) findViewById(R.id.lista_billetes);

        if(list.size()==0)
        {
            list.add(new ElementoSpinner(-1,"Aún no tiene billetes registrados"));
            vacio = true;
            mPos = 0;
        }
        else
        {
            vacio = false;
            if(mPos>=list.size())
            {
                mPos = list.size()-1;
            }
        }

        ArrayAdapter<ElementoSpinner> dataAdapter = new ArrayAdapter<ElementoSpinner>
                (this, R.layout.my_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(dataAdapter);

        spinner1.setSelection(mPos);

    }

    private void showResult(int posicion)
    {
        mPos = posicion;
        if(!vacio)
            mFragmento.setPosicion(getIdSpinner());
        else
            mFragmento.setPosicion(-1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lista_billetes, menu);
        return true;
    }

    public void comprar(View v)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.loteria.cl/movilnet/"));
        startActivity(browserIntent);
    }

    public void reload(View v)
    {
        mFragmento.setText();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_comprar) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.loteria.cl/movilnet/"));
            startActivity(browserIntent);
            return true;
        }
        if (id == R.id.action_capture || id == R.id.action_agregar) {
            Intent intent = new Intent(this, Captura_Billete.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_eliminar && !vacio) {
            confirmarEliminar(getIdSpinner());
            return true;
        }

        if (id == R.id.action_renombrar && !vacio) {
            inputRenombrar(getIdSpinner());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private int getIdSpinner(){
        Spinner spinner = ((Spinner) findViewById(R.id.lista_billetes));
        ElementoSpinner elementoSpinner = (ElementoSpinner) spinner.getSelectedItem();
        return  elementoSpinner.getId();
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
        List<ElementoSpinner> list = Controlador_Lista.getNombres();
        setUpSpinner(list);

        showResult(mPos);

    }

}
