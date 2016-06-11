package com.guzman.henrry.arduinobluetooth;

/**
 * Created by WINDOWS on 10/5/2016.
 */

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
public class ReconocimientoVoz extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_microfono);
        final ImageButton reconoce=(ImageButton) findViewById(R.id.imageButton);
        reconoce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//Este es el intent del reconocimiento de voz
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//Especificamos el idioma, en esta ocasión probé con el de Estados Unidos
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                //Iniciamos la actividad dentro de un Try en caso surja un error.
                try {
                    reconoce.setBackgroundResource(R.drawable.microfonobuscando);
                    startActivityForResult(intent, 1);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(), "Tu dispositivo no soporta el reconocimiento de voz", Toast.LENGTH_LONG).show();
                }

            }


        });
    }

    public void onActivityResult(int requestcode, int resultcode, Intent datos)
    {

// Si el reconocimiento de voz es correcto almacenamos dentro de un array los datos obtenidos.
//Mostramos en pantalla el texto obtenido de la posición 0.
        if (resultcode== Activity.RESULT_OK && datos!=null)
        {
            ImageButton reconoce=(ImageButton) findViewById(R.id.imageButton);
            ArrayList<String> text=datos.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Toast.makeText(this,text.get(0),Toast.LENGTH_LONG).show();
            reconoce.setBackgroundResource(R.drawable.microfonoapagado);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}