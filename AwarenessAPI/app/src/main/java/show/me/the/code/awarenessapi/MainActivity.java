package show.me.the.code.awarenessapi;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.DetectedActivityFence;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
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

        AwarenessFence walkingFence = DetectedActivityFence.during(DetectedActivityFence.WALKING);

        Awareness.FenceApi.updateFences(client, new FenceUpdateRequest.Builder().addFence("walkingFence", walkingFence, pendingIntent).build())
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            Toast.makeText(MainActivity.this, "Fence has been registered", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(myFenceReceiver);
    }

    public class FenceReveiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
