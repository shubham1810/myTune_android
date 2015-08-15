package com.shubhamdokania.mytune;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.shubhamdokania.mytune.API.gitapi;
import com.shubhamdokania.mytune.model.Datum;
import com.shubhamdokania.mytune.model.SearchResults;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    Button searchButton;
    EditText searchText;
    ListView searchResults;

    JSONAdapter mJSONAdapter;

    private void searchForMusic(String searchString) {
        //Toast.makeText(getApplicationContext(), searchString, Toast.LENGTH_LONG).show();

        // Here goes the method to get the data from the server
        String API = "http://192.168.43.21:8080";

        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).
                setEndpoint(API).build();

        gitapi git = restAdapter.create(gitapi.class);

        git.getFeed(searchString, new Callback<SearchResults>() {
            @Override
            public void success(SearchResults searchResults, Response response) {
                //Log.d("SOmetihng is bothering me", "======" + response.getStatus());
                mJSONAdapter.updateData(searchResults.getData());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "SomeError", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);

        searchText = (EditText) findViewById(R.id.search_input);

        searchResults = (ListView) findViewById(R.id.search_results);

        searchResults.setOnItemClickListener(this);

        mJSONAdapter = new JSONAdapter(this, getLayoutInflater());
        searchResults.setAdapter(mJSONAdapter);
    }

    @Override
    public void onClick(View v) {
        searchForMusic(searchText.getText().toString());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Datum mDataObject = (Datum) mJSONAdapter.getItem(position);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Download");
        alert.setMessage("Do you want to download " + mDataObject.getName() + " ?");

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });

        alert.show();
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
}
