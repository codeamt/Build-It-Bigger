package builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.droidjokes.DroidJokes;
import com.example.javajokes.Joker;
import com.udacity.gradle.builditbigger.R;


public class MainActivity extends AppCompatActivity implements EndpointAsyncTask.Callback {
    Joker joker = new Joker();

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
        Intent jokeIntent = new Intent(this, DroidJokes.class);
        jokeIntent.putExtra(Intent.EXTRA_TEXT, result);
        startActivity(jokeIntent);
    }
}
