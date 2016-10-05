package show.me.the.code.awarenessapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.DetectedActivityFence;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.content.Context context;
        GoogleApiClient client = new GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .build();
        client.connect();

        AwarenessFence walkingFence = DetectedActivityFence.during(DetectedActivityFence.WALKING);

    }
}
