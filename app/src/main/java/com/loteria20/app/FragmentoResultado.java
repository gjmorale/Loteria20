package com.loteria20.app;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentoResultado extends Fragment {

    private int posicion;
    private static int idBase = 1234;

    public void setPosicion(int p)
    {
        boolean cambio = false;
        if(posicion != p)
            cambio = true;
        posicion = p;
        if(cambio)
        {
            setText();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resultado,
                container, false);
        return view;

    }
    @Override
    public void onResume()
    {
        super.onResume();

        setText();
    }

    public void setText()
    {
        TextView view = (TextView) getActivity().findViewById(R.id.resultado_txt);
        if(posicion == -1)
        {
            view.setText("Ingrese un billete para ver su resultado.\n Utilice el icono de la cámara\npara registrar un nuevo billete");
        }
        else
        {
            view.setText("Cargando resultados ...");
            AsyncTask api;
            if(Integer.parseInt(Controlador_Lista.getEstado(posicion)) < 2)
            {
                try
                {
                    api = new APILoteria().execute(Controlador_Lista.getCodigo(posicion));
                    String resp = (String)api.get();
                    Controlador_Lista.setResultado(posicion,resp);
                    Toast.makeText(getActivity(), "Solicitando a Loteria.cl", Toast.LENGTH_LONG).show();
                }
                catch(Exception e)
                {
                    view.setText(e.toString());
                }
            }
            Boleto res = new Boleto(Controlador_Lista.getResultado(posicion));
            String newEstado = String.valueOf(setUpResultado(res));
            Controlador_Lista.setEstado(posicion, newEstado);
            //view.setText(Controlador_Lista.getResultado(posicion));
            view.setText("Resultado");
        }
    }

    private int setUpResultado(Boleto boleto)
    {
        LinearLayout view_lista = (LinearLayout)getActivity().findViewById(R.id.dinamico);
        if(boleto.estado() == 2)
        {
            view_lista.removeAllViews();
            setJuego("Numeros del cartón:",boleto.getNumeros(), new int[]{-1},view_lista);
            if(boleto.kino)
                setJuego("Aciertos en Kino:",boleto.getNumeros(), boleto.listaKino(),view_lista);
            if(boleto.reKino)
                setJuego("Aciertos en ReKino:",boleto.getNumeros(), boleto.listaReKino(),view_lista);
            if(boleto.chanchito)
                setJuego("Aciertos en Chanchito Regalón:",boleto.getNumeros(), boleto.listaChanchito(),view_lista);
            if(boleto.ganaMas)
                setJuego("Aciertos en Gana Más", boleto.getNumeros(), boleto.listaGanaMas(), view_lista);
            if(boleto.comboMarraqueta)
            {
                for(int i = 0; i<boleto.listasComboMarraqueta().length ; i++)
                {
                    setJuego("Combo Marraqueta sorteo n°"+(i+1), boleto.getNumeros(), boleto.listasComboMarraqueta()[i], view_lista);
                }
            }
            if(boleto.chaoJefe)
            {
                for(int i = 0; i<boleto.listasChaoJefe().length ; i++)
                {
                    setJuego("Chao Jefe sorteo n°"+(i+1), boleto.getNumeros(), boleto.listasChaoJefe()[i], view_lista);
                }
            }

            setPremios(boleto.premios());

            ((LinearLayout)getActivity().findViewById(R.id.resultado)).setVisibility(View.VISIBLE);
            ((TextView)getActivity().findViewById(R.id.anuncio)).setVisibility(View.GONE);
            ((Button)getActivity().findViewById(R.id.button_comprar)).setVisibility(View.VISIBLE);
        }
        else
        {
            ((LinearLayout)getActivity().findViewById(R.id.resultado)).setVisibility(View.GONE);
            ((TextView)getActivity().findViewById(R.id.anuncio)).setVisibility(View.VISIBLE);
            ((Button)getActivity().findViewById(R.id.button_comprar)).setVisibility(View.GONE);

            ((TextView)getActivity().findViewById(R.id.anuncio)).setText(boleto.respuesta() + "\n" + boleto.respuestaOriginal);
        }
        return boleto.estado();
    }

    private void setPremios(int[] montos)
    {
        String premio="";
        String sPremio = "Kino:\nReKino:\nChao Jefe:\nCombo Marraqueta:\nGana Más\nChanchito Regalón:\nTotal:";
        int total = 0;
        for(int i = 0;i<montos.length; i++)
        {
            premio += "$ " + montos[i] + "\n";
            total += montos[i];
        }
        premio += "$ " + total;

        ((TextView)getActivity().findViewById(R.id.premios_int)).setText(premio);
        ((TextView)getActivity().findViewById(R.id.premios_txt)).setText(sPremio);
    }

    private void setJuego(String nombre, int[] numeros, int[] check, LinearLayout ll)
    {
        float proporcion = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
        RelativeLayout rl2 = new RelativeLayout(getActivity());
        rl2.setBackgroundResource(R.drawable.verde);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(proporcion*70));

        TextView n = new TextView(getActivity());
        n.setText(nombre);
        n.setTextSize(proporcion * 15);
        n.setTextColor(Color.WHITE);
        n.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        LinearLayout.LayoutParams nParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        nParams.setMargins(0,5,0,0);

        ll.addView(n);
        ll.addView(rl2);

        setNumeros(numeros,check,rl2);
    }

    private void setNumeros(int[] numeros, int[] check, RelativeLayout rl)
    {
        TextView numero;
        RelativeLayout.LayoutParams params;
        int j = 0;
        float proporcion = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
        int height = (int)(25*proporcion);
        int width = height;
        int par;
        for(int i = 0; i< numeros.length; i++)
        {
            par = i%2;
            numero = new TextView(getActivity());
            numero.setText(String.valueOf(numeros[i]));
            idBase++;
            numero.setId(idBase);
            numero.setTextSize(15*proporcion);
            params = new RelativeLayout.LayoutParams(width,height);
            if(i != 0)
            {
                if(par==0)
                {
                    params.addRule(RelativeLayout.RIGHT_OF, (idBase-2));
                    params.setMargins(2, 8, 2, 2);
                }
                else
                {
                    if(i == 1)
                    {
                        params.addRule(RelativeLayout.BELOW, (idBase-1));
                        params.setMargins(8, 2, 2, 8);
                    }
                    else
                    {
                        params.addRule(RelativeLayout.RIGHT_OF, (idBase-2));
                        params.addRule(RelativeLayout.BELOW, (idBase-1));
                        params.setMargins(2, 2, 2, 8);
                    }
                }
            }
            else
            {
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.setMargins(8, 8, 2, 2);
            }
            numero.setPadding(0,0,5,5);
            numero.setGravity(Gravity.CENTER);
            numero.setLayoutParams(params);
            numero.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            if(j<check.length && check[j]==numeros[i])
            {
                j++;
                numero.setBackgroundResource(R.drawable.pelotacheck);
            }
            else
            {
                numero.setBackgroundResource(R.drawable.pelota);
            }
            rl.addView(numero);
        }
    }
}