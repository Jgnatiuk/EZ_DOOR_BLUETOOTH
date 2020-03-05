package com.example.ez_door_bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;

    String msg;

    TextView mStatusBlueTv, mPairedTv;
    ImageView mBlueIv;
    Button mOnBtn, mOffBtn, mDiscoverableBtn, mPairedBtn;

    BluetoothAdapter mBlueAdapter;

    // TEST FOR COMMIT

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusBlueTv = findViewById(R.id.statusBluetoothTv);
        mPairedTv = findViewById(R.id.pairedTv);
        mBlueIv = findViewById(R.id.BluetoothIv);
        mOnBtn = findViewById(R.id.onBtn);
        mOffBtn = findViewById(R.id.offBtn);
        mDiscoverableBtn = findViewById(R.id.discoverableBtn);
        mPairedBtn = findViewById(R.id.pairedBtn);

        //adapter
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();

        //check if bluetooth is available
        if(mBlueAdapter == null){
            mStatusBlueTv.setText("Bluetooth is not available");
            mBlueIv.setImageResource(R.drawable.ic_action_off);
            return;
        }
        else mStatusBlueTv.setText("Bluetooth is available");

        //set image according to bluetooth status(on/off)
        if(mBlueAdapter.isEnabled()) mBlueIv.setImageResource(R.drawable.ic_action_on);
        else mBlueIv.setImageResource(R.drawable.ic_action_off);

        //on btn click
        mOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!mBlueAdapter.isEnabled()) {

                    Toast.makeText(getApplicationContext(), "Turning On Bluetooth...", Toast.LENGTH_SHORT).show();
                    //intent to on bluetooth
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);

                }
                else Toast.makeText(getApplicationContext(), "Bluetooth is already turned on", Toast.LENGTH_SHORT).show();
            }
        });

        //discoverable btn click
        mDiscoverableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                if(!mBlueAdapter.isDiscovering()){

                    Toast.makeText(getApplicationContext(), "Making your device discoverable", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent, REQUEST_DISCOVER_BT);

                }

            }
        });

        //off btn click
        mOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                if(mBlueAdapter.isEnabled()){

                    mBlueAdapter.disable();
                    Toast.makeText(getApplicationContext(), "Turning bluetooth off", Toast.LENGTH_SHORT).show();
                    mBlueIv.setImageResource(R.drawable.ic_action_off);

                }
                else Toast.makeText(getApplicationContext(), "Bluetooth is already disabled", Toast.LENGTH_SHORT).show();

            }
        });

        //get paired devices btn click
        mPairedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                if(mBlueAdapter.isEnabled()){

                    mPairedTv.setText("Paired Devices");
                    Set<BluetoothDevice> devices = mBlueAdapter.getBondedDevices();
                    for(BluetoothDevice device: devices){

                        mPairedTv.append("\nDevice" + device.getName() + "," + device);

                    }

                }
                //bluetooth is off so cannot get paired devices
                else Toast.makeText(getApplicationContext(), "Turn on bluetooth to get paired devices", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {

        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK){
                    //bluetooth is on
                    mBlueAdapter.enable();
                    mBlueIv.setImageResource(R.drawable.ic_action_on);
                    Toast.makeText(getApplicationContext(), "Bluetooth is on", Toast.LENGTH_SHORT).show();
                }
                //user denied to turn bluetooth on
                else Toast.makeText(getApplicationContext(), "Couldn't turn on bluetooth", Toast.LENGTH_SHORT).show();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);


    }
}
