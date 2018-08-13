package builditbigger;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

public class EndpointAsyncTask extends AsyncTask<Void, Void, String> {

    private  static MyApi myApi = null;
    private Callback callback;

    public interface Callback {
        void onFinished(String result);
    }

    EndpointAsyncTask(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(Void...params) {
        if(myApi == null) {
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> request) throws IOException {
                            request.setDisableGZipContent(true);
                        }
                    })
                    .setApplicationName("Jokes App");
            myApi = builder.build();
        }

        try {
            return myApi.fetchJoke().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }

    }

    @Override
    protected void onPostExecute(String result) {
        if(result != null) {
            callback.onFinished(result);
        }
    }
}
