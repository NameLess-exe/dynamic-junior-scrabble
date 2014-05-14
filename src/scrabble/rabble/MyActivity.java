package com.leoxiong.clientservertest;

import android.app.Activity;
import android.content.*;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.leoxiong.clientservertest.Client.ClientService;
import com.leoxiong.clientservertest.Serialization.*;
import com.leoxiong.clientservertest.Server.ServerService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class MyActivity extends Activity {
    private static final String TAG = "MyActivity";

    private ClientService clientService;
    private Intent clientServiceIntent;
    private Intent serverServiceIntent;
    private ToggleButton serverButton;
    private ScrollView scrollView;
    private Button pingButton;
    private ServerService serverService;
    private ToggleButton beaconButton;
    private LinearLayout messagesLayout;
    private Button sendButton;
    private EditText chatEditText;
    private Button clearLogButton;
    private BroadcastReceiver action;
    private LocalBroadcastManager localBroadcastManager;
    private String mac;
    private View.OnClickListener logMessageClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == null)
                return;
            switch (((Sendable) view.getTag()).getType()) {
                case UDP_BEACON:
                    UDPBeacon action = (UDPBeacon) view.getTag();
                    clientService.connect(action.getIp(), action.getPort());
                    log(String.format("connected to %1$s:%2$s", action.getIp(), action.getPort()));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        serverServiceIntent = new Intent(this, ServerService.class);
        clientServiceIntent = new Intent(this, ClientService.class);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        action = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Sendable action = (Sendable) intent.getSerializableExtra(ClientService.ACTION);
                switch (action.getType()) {
                    case UDP_BEACON:
                        UDPBeacon beacon = (UDPBeacon) action;
                        log(String.format("server discovered at %1$s:%2$s", beacon.getIp(), beacon.getPort()), beacon);
                        break;
                    case PING:
                        Ping ping = (Ping) action;
                        log(String.format("ping data - %1$s", ping.getData()));
                        break;
                    case LOG:
                        Ping log = (Ping) action;
                        log(log.getData());
                        break;
                    default:
                        log(String.format("received unknown json object: %1$s", action.toString()));
                }
            }
        };


        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        mac = info.getMacAddress();

        serverButton = (ToggleButton) findViewById(R.id.serverButton);
        messagesLayout = (LinearLayout) findViewById(R.id.messagesLayout);
        clearLogButton = (Button) findViewById(R.id.clearLogButton);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        pingButton = (Button) findViewById(R.id.pingButton);
        beaconButton = (ToggleButton) findViewById(R.id.beaconButton);
        sendButton = (Button) findViewById(R.id.sendButton);
        chatEditText = (EditText) findViewById(R.id.chatEditText);

        serverButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    startService(serverServiceIntent);
                else
                    stopService(serverServiceIntent);
            }
        });

        clearLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messagesLayout.removeAllViews();
            }
        });

        pingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clientService.getTcpClient().send(new Ping(String.valueOf(System.currentTimeMillis())));
                log("Pinged server");
            }
        });

        beaconButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    serverService.getUdpBeacon().resumeBeacon();
                else
                    serverService.getUdpBeacon().pauseBeacon();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = chatEditText.getText().toString();
                clientService.getTcpClient().send(new Ping(text + " from " + mac));
            }
        });

        bindService(serverServiceIntent, serverServiceConnection, Context.BIND_AUTO_CREATE);
        bindService(clientServiceIntent, clientServiceConnection, Context.BIND_AUTO_CREATE);
        startService(clientServiceIntent);
    }

    @Override
    protected void onResume() {
        localBroadcastManager.registerReceiver(action, new IntentFilter(ClientService.ACTION));

        super.onResume();
    }

    @Override
    protected void onPause() {
        localBroadcastManager.unregisterReceiver(action);

        super.onPause();
    }

    @Override
    public CharSequence onCreateDescription() {
        stopService(serverServiceIntent);
        stopService(clientServiceIntent);

        return super.onCreateDescription();
    }

    private ServiceConnection clientServiceConnection = new ServiceConnection() {
        private static final String TAG = "ClientServiceConnection";

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            clientService = ((ClientService.LocalBinder) iBinder).getInstance();
            Log.i(TAG, "ClientService connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG, "ClientService disconnected");
        }
    };

    private ServiceConnection serverServiceConnection = new ServiceConnection() {
        private static final String TAG = "ClientServiceConnection";

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            serverService = ((ServerService.LocalBinder) iBinder).getInstance();
            Log.i(TAG, "ServerService connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG, "ServerService disconnected");
        }
    };

    private void log(String message) {
        log(message, null);
    }

    private void log(String message, Object tag) {
        TextView view = (TextView) LayoutInflater.from(this).inflate(R.layout.message, null);
        view.setOnClickListener(logMessageClick);
        view.setTag(tag);
        view.setText(message);
        messagesLayout.addView(view);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
