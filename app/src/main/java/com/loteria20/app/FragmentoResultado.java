package com.loteria20.app;

        import android.app.Fragment;
        import android.graphics.Typeface;
        import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentoResultado extends Fragment {

    private int posicion;
    private boolean checked = false;
    private static int idBase = 1234;

    public void setPosicion(int p)
    {
        posicion = p;
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
                    Boleto res = new APILoteria().execute("03460035636774").get();
                    //view.setText(res.respuestaOriginal);
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
            setNumeros(boleto.getNumeros(),boleto.getNumeros(), (RelativeLayout)getActivity().findViewById(R.id.numeros));

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

            String premio="";
            int total = 0;
            for(int i = 0;i<boleto.premios().length; i++)
            {
                premio +="\t\t\t" + boleto.premios()[i] + "\n";
                total += boleto.premios()[i];
            }
            premio += "Total:\t\t" + total;
            ((TextView)getActivity().findViewById(R.id.premios_txt)).setText(premio);

            ((LinearLayout)getActivity().findViewById(R.id.resultado)).setVisibility(View.VISIBLE);
            ((TextView)getActivity().findViewById(R.id.anuncio)).setVisibility(View.GONE);

            return true;
        }
        else
        {
            ((LinearLayout)getActivity().findViewById(R.id.resultado)).setVisibility(View.GONE);
            ((TextView)getActivity().findViewById(R.id.anuncio)).setVisibility(View.VISIBLE);
            ((TextView)getActivity().findViewById(R.id.anuncio)).setText(boleto.respuesta());
            return false;
        }
    }

    private void setNumeros(int[] numeros, int[] check, RelativeLayout rl)
    {
        TextView numero;
        RelativeLayout.LayoutParams params;
        int j = 0;
        for(int i = 0; i< numeros.length; i++)
        {
            numero = new TextView(getActivity());
            numero.setText(String.valueOf(numeros[i]));
            idBase++;
            numero.setId(idBase);
            params = new RelativeLayout.LayoutParams(20,20);
            if(i != 0)
            {
                params.addRule(RelativeLayout.RIGHT_OF, (idBase-1));
                params.setMargins(2, 8, 2, 8);
            }
            else
            {
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.setMargins(8, 8, 2, 8);
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