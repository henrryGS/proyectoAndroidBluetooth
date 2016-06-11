package com.guzman.henrry.arduinobluetooth;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;


public class ledControl extends AppCompatActivity implements TextToSpeech.OnInitListener {

    Button btnOn, btnOff, btnDis;
    SeekBar brightness;
    TextView lumn;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private TextToSpeech tts;

    private Boolean activado=false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);




        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //recivimos la mac address obtenida en la actividad anterior

        setContentView(R.layout.activity_led_control);

        //Habla codigo
        tts = new TextToSpeech( this, this );

        btnOn = (Button)findViewById(R.id.button2);
        btnOff = (Button)findViewById(R.id.button3);
        btnDis = (Button)findViewById(R.id.button4);
        brightness = (SeekBar)findViewById(R.id.seekBar);
        lumn = (TextView)findViewById(R.id.lumn);

        new ConnectBT().execute(); //Call the class to connect



        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!activado){
                    dime_algo("El foco se a activado");
                    turnOnLed();
                    ImageButton imagenFoco=(ImageButton) findViewById(R.id.imageButtonFoco);
                    imagenFoco.setBackgroundResource(R.drawable.focoprendido100);
                    activado=true;
                }else{
                    dime_algo("El foco ya se encuentra activado, no insista");
                }

            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(activado){
                    dime_algo("El foco se a desactivado");
                    turnOffLed();
                    ImageButton imagenFoco=(ImageButton) findViewById(R.id.imageButtonFoco);
                    imagenFoco.setBackgroundResource(R.drawable.focoapagado100);
                    activado=false;
                }else{
                    dime_algo("El foco ya se encuentra desactivado, no insista");
                }

            }
        });

        btnDis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Disconnect();
            }
        });

        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true) {
                    lumn.setText(String.valueOf(progress));
                    try {
                        btSocket.getOutputStream().write(String.valueOf(progress).getBytes());
                    } catch (IOException e) {

                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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
                    reconoce.setBackgroundResource(R.drawable.microfonoapagado);
                    startActivityForResult(intent, 1);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(), "Tu dispositivo no soporta el reconocimiento de voz", Toast.LENGTH_LONG).show();
                }

            }


        });


    }

    private void Disconnect()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.close();
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish();

    }

    private void turnOffLed()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("TF".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOnLed()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("TO".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }


    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(ledControl.this, "Conexión...", "\n" +
                    "por favor espera!!!");
        }

        @Override
        protected Void doInBackground(Void... devices)
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                 myBluetooth = BluetoothAdapter.getDefaultAdapter();
                 BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//conectamos al dispositivo y chequeamos si esta disponible
                 btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                 BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                 btSocket.connect();
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Conexión Fallida");
                finish();
            }
            else
            {
                msg("Conectado");
                isBtConnected = true;
            }
            progress.dismiss();
        }
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
            reconoce.setBackgroundResource(R.drawable.microfonobuscando);

            if (text.get(0).equals("Encender")||text.get(0).equals("encender")||text.get(0).equals("On")||text.get(0).equals("on")){
                if(!activado){
                    turnOnLed();
                    ImageButton imagenFoco=(ImageButton) findViewById(R.id.imageButtonFoco);
                    imagenFoco.setBackgroundResource(R.drawable.focoprendido100);
                    dime_algo("Comando correcto, El foco se a activado");
                    activado=true;
                }else{
                    dime_algo("El foco ya se encuentra activado, no insista");
                }
            }else if (text.get(0).equals("Apagar")||text.get(0).equals("apagar")||text.get(0).equals("Off")||text.get(0).equals("off")){
                if(activado){
                    turnOffLed();
                    ImageButton imagenFoco=(ImageButton) findViewById(R.id.imageButtonFoco);
                    imagenFoco.setBackgroundResource(R.drawable.focoapagado100);
                    dime_algo("Comando correcto, El foco se a desactivado");
                    activado=false;
                }else{
                    dime_algo("El foco ya se encuentra desactivado, no insista");
                }

            }else{
                dime_algo("Comando incorrecto, No existe comando de voz");
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onInit(int status) {
           if ( status == TextToSpeech.SUCCESS ) {
                //coloca lenguaje por defecto en el celular, en nuestro caso el lenguaje es aspañol ;)
                int result = tts.setLanguage( Locale.getDefault() );

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {

                     Log.e("TTS", "This Language is not supported");
                    } else {

                    }

               } else {
                Log.e("TTS", "Initilization Failed!");
               }
         }

              /**
     103  * metodo para convertir texto a voz
     104  * @param String texto
     105  * */
              private void dime_algo( String texto ) {
           tts.speak( texto, TextToSpeech.QUEUE_FLUSH, null );
          }

              //Cuando se cierra la aplicacion se destruye el TTS
              @Override
      public void onDestroy() {
           if (tts != null) {
                tts.stop();
                tts.shutdown();
               }
           super.onDestroy();
        }



}
