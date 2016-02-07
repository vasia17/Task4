package com.example.shon.boost4;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.shon.boost4.service.AccelerometerService;
import com.firebase.client.Firebase;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    public static final String MAIN_TAG = "my_app";
    private Context mContext = this;
    private boolean mIsBound;
    private ServiceConnection mConnection = new ServiceConnection() {
        private AccelerometerService mAccBoundService;
        public void onServiceConnected(ComponentName className, IBinder service) {
            mAccBoundService = ((AccelerometerService.AccelerometerBinder)service).getService();
            Toast.makeText(mContext, R.string.local_service_connected,
                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            mAccBoundService = null;
            Toast.makeText(mContext, R.string.local_service_disconnected,
                    Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.switch_control);
        switchCompat.setOnCheckedChangeListener(this);

        Firebase.setAndroidContext(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(MAIN_TAG, "MainActivity: onCheckedChanged " + isChecked);
        if (isChecked){
            doBindService();
        } else {
            doUnbindService();
        }
    }

    void doBindService() {
        Log.d(MAIN_TAG, "MainActivity: doBindService");
        bindService(new Intent(mContext, AccelerometerService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        Log.d(MAIN_TAG, "MainActivity: doUnbindService");
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(MAIN_TAG, "MainActivity: onDestroy");
        super.onDestroy();
        doUnbindService();
    }

}
