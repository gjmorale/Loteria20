package com.loteria20.app;

import android.app.Fragment;
import android.graphics.Typeface;
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

public class FragmentoResultado extends Fragment {

    private int posicion;
    private boolean checked = false;
    private static int idBase = 1234;

    public void setPosicion(int p)
    {
        boolean cambio = false;
        if(posicion != p)
            cambio = true;
        posicion = p;
        if(cambio)
        {
            checked = false;
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
            if(!checked)
            {
                try
                {
                    view.setText("RESULTADOS:");
                    Boleto res = new APILoteria().execute(Controlador_Lista.getCodigo(posicion)).get();
                    checked = setUpResultado(res);
                }catch(Exception e)
                {
                    view.setText(e.toString());
                }
            }
        }
    }

    private boolean setUpResultado(Boleto boleto)
    {
        LinearLayout view_lista = (LinearLayout)getActivity().findViewById(R.id.lista_resultados);
        if(boleto.sorteoRealizado())
        {
            setNumeros(boleto.getNumeros(),new int[]{-1}, (RelativeLayout)getActivity().findViewById(R.id.numeros));

            if(boleto.kino())
            {
                setNumeros(boleto.getNumeros(), boleto.listaKino(), (RelativeLayout) getActivity().findViewById(R.id.numerosKino));
                getActivity().findViewById(R.id.kino).setVisibility(View.VISIBLE);
            }

            if(boleto.reKino())
            {
                setNumeros(boleto.getNumeros(), boleto.listaReKino(), (RelativeLayout) getActivity().findViewById(R.id.numerosReKino));
                getActivity().findViewById(R.id.reKino).setVisibility(View.VISIBLE);
            }

            if(boleto.chanchito())
            {
                setNumeros(boleto.getNumeros(), boleto.listaChanchito(), (RelativeLayout) getActivity().findViewById(R.id.numerosChanchito));
                getActivity().findViewById(R.id.chanchito).setVisibility(View.VISIBLE);
            }

            setPremios(boleto.premios());

            ((LinearLayout)getActivity().findViewById(R.id.resultado)).setVisibility(View.VISIBLE);
            ((TextView)getActivity().findViewById(R.id.anuncio)).setVisibility(View.GONE);
            ((Button)getActivity().findViewById(R.id.button_comprar)).setVisibility(View.VISIBLE);

            return true;
        }
        else
        {
            ((LinearLayout)getActivity().findViewById(R.id.resultado)).setVisibility(View.GONE);
            ((TextView)getActivity().findViewById(R.id.anuncio)).setVisibility(View.VISIBLE);
            ((Button)getActivity().findViewById(R.id.button_comprar)).setVisibility(View.GONE);
            ((TextView)getActivity().findViewById(R.id.anuncio)).setText(boleto.respuesta()+"\n\n"+Controlador_Lista.getCodigo(posicion));
            return false;
        }
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
            numero.setTextSize(12*proporcion);
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