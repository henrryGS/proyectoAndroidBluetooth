package com.guzman.henrry.arduinobluetooth;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    public static final int segundos=5;
    public static final int milisegundos=segundos*1000;
    public static final int delay=2;
    private ProgressBar progreso;

    ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagen=(ImageView)findViewById(R.id.imageViewInicio);
        imagen.setBackgroundResource(R.drawable.arduinoandroid);

        progreso=(ProgressBar)findViewById(R.id.progressBarInicio);
        progreso.setMax(max_progreso());
        empezarAnimacion();
    }

    public void empezarAnimacion(){
        new CountDownTimer(milisegundos,1000) {
            @Override
            public void onTick(long l) {
                progreso.setProgress(establecer_progreso(l));
            }

            @Override
            public void onFinish() {
                //Intent intentNuevo=new Intent(MainActivity.this,PrincipalActivity.class);
                Intent intentNuevo=new Intent(MainActivity.this,DeviceList.class);
                //Intent intentNuevo=new Intent(MainActivity.this,ReconocimientoVoz.class);
                startActivity(intentNuevo);
                finish();
            }
        }.start();
    }

    public int establecer_progreso(long milisecons){

        return (int)(milisegundos-milisecons)/1000;
    }

    public int max_progreso(){
     return segundos-delay;
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
