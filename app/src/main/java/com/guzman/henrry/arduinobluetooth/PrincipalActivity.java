package com.guzman.henrry.arduinobluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by HENRRY GUZMAN on 3/5/2016.
 */
public class PrincipalActivity extends AppCompatActivity{

    BluetoothAdapter bluetoothAdapter;
    Button botonActivaBluetooth;
    ArrayList<BluetoothDevice> arrayDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        botonActivaBluetooth=(Button)findViewById(R.id.buttonActivaBluetooth);
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter==null){
            cambiaEstiloBotonDesactivadoBluetooth();

        }else{
            cambiaEstiloBotonActivadoBluetooth();

        }
    }



    public void activaBluetooth(View view){
        setEstadoBluetooth();


    }

    public void setEstadoBluetooth(){
        if(bluetoothAdapter.isEnabled()){
            bluetoothAdapter.disable();

            cambiaEstiloBotonDesactivadoBluetooth();
        }else{
            bluetoothAdapter.enable();

            cambiaEstiloBotonActivadoBluetooth();
        }
    }

    public void cambiaEstiloBotonActivadoBluetooth(){

        botonActivaBluetooth.setBackgroundResource(R.color.botonColorBluetoothActivadoBackground);
        botonActivaBluetooth.setTextColor(0xff000000);
        botonActivaBluetooth.setText(R.string.bluetooth_activado);
    }

    public void cambiaEstiloBotonDesactivadoBluetooth(){
        botonActivaBluetooth.setBackgroundResource(R.color.botonColorBluetoothDesactivadoBackground);
        botonActivaBluetooth.setTextColor(0xffFFFFFF);
        botonActivaBluetooth.setText(R.string.bluetooth_desactivado);
    }

    public void buscaDispositivos(View view){
        arrayDevices.clear();

        // Comprobamos si existe un descubrimiento en curso. En caso afirmativo, se
        // cancela.
        if(bluetoothAdapter.isDiscovering())
            bluetoothAdapter.cancelDiscovery();

        // Iniciamos la busqueda de dispositivos
        if(bluetoothAdapter.startDiscovery())
            // Mostramos el mensaje de que el proceso ha comenzado
            Toast.makeText(this, R.string.IniciandoDescubrimiento, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, R.string.ErrorIniciandoDescubrimiento, Toast.LENGTH_SHORT).show();
    }
}
