package show.me.the.code.awarenessapi;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.HeadphoneFence;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity {
    private static final String FENCE_RECEIVER_ACTION =
            "show.me.the.code.awarenessapi";
    private PendingIntent pendingIntent;
    private FenceReveiver myFenceReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(FENCE_RECEIVER_ACTION);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent , 0);
        myFenceReceiver = new FenceReveiver();
        registerReceiver(myFenceReceiver, new IntentFilter(FENCE_RECEIVER_ACTION));

        GoogleApiClient client = new GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .build();
        client.connect();

        AwarenessFence headphoneFence = HeadphoneFence.during(HeadphoneState.PLUGGED_IN);

        Awareness.FenceApi.updateFences(client, new FenceUpdateRequest.Builder().addFence("headphoneFence", headphoneFence, pendingIntent).build())
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            Toast.makeText(MainActivity.this, "Fence has been registered", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Fence has not been registered", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(myFenceReceiver);
        }
        catch (IllegalArgumentException e ) {
        }

    }

    public class FenceReveiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            FenceState fenceState = FenceState.extract(intent);
            if (fenceState.getFenceKey().equals("headphoneFence")) {
                switch (fenceState.getCurrentState()) {
                    case FenceState.TRUE:
                        Toast.makeText(MainActivity.this, "HEADPHONES PLUGGED!", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }
    }
}
