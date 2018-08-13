package builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.droidjokes.DroidJokes;
import com.example.javajokes.Joker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.udacity.gradle.builditbigger.R;


public class MainActivity extends AppCompatActivity implements EndpointAsyncTask.Callback {
    //Joker joker = new Joker();
    Button jokeButton;
    public InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void tellJoke(View view) {
        //grab joke from java lib
        new EndpointAsyncTask(this).execute();
        //Toast.makeText(this, joke, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinished(String result) {
        final Intent jokeIntent = new Intent(this, DroidJokes.class);
        jokeIntent.putExtra(Intent.EXTRA_TEXT, result);
        View fragmentView = getSupportFragmentManager().findFragmentById(R.id.fragment).getView();
        if(fragmentView != null) {
            jokeButton = fragmentView.findViewById(R.id.jokeButton);
            jokeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getApplicationContext() != null) {
                        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_ad_unit_id));
                        mInterstitialAd = new InterstitialAd(getApplicationContext());
                        mInterstitialAd.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
                        mInterstitialAd.loadAd(new AdRequest.Builder()
                                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                                .build());
                    }
                    if(mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                        Log.v("From Frag", "ad launched");
                    } else {
                        Log.d("ON CLICK AD: ", "The Ad wasn't loaded.");
                    }

                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            startActivity(jokeIntent);
                        }
                    });
                }
            });
        }
        //startActivity(jokeIntent);
    }
}
