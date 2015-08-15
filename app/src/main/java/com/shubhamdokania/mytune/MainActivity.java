package com.shubhamdokania.mytune;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.shubhamdokania.mytune.model.EmptyData;
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

    public static String API = "http://128.199.128.227:8198/";

    JSONAdapter mJSONAdapter;

    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private DownloadManager downloadManager;
    private long downloadReference;

    private void searchForMusic(String searchString) {
        //Toast.makeText(getApplicationContext(), searchString, Toast.LENGTH_LONG).show();

        // Here goes the method to get the data from the server
        String API = "http://128.199.128.227:8198/";

        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).
                setEndpoint(API).build();

        gitapi git = restAdapter.create(gitapi.class);

        git.getFeed(searchString, new Callback<SearchResults>() {
            @Override
            public void success(SearchResults searchResults, Response response) {
                //Log.d("Something is bothering me", "======" + response.getStatus());
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
        final Datum mDataObject = (Datum) mJSONAdapter.getItem(position);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Download");
        alert.setMessage("Do you want to download " + mDataObject.getName() + " ?");

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), mDataObject.getName(), Toast.LENGTH_LONG).show();
                String downloadURL = API + "/api/download/music?id=" + mDataObject.getId() + "&name=" + mDataObject.getName();
                RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).
                        setEndpoint(API).build();

                gitapi git = restAdapter.create(gitapi.class);

                git.getLink(mDataObject.getId(), mDataObject.getName(), new Callback<EmptyData>() {
                    @Override
                    public void success(EmptyData emptyData, Response response) {
                        Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                });
                startDownload(downloadURL.replaceAll(" ", "%20"), mDataObject.getName());
                Toast.makeText(getApplicationContext(), "File Downloaded", Toast.LENGTH_LONG).show();
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

    /* ---------------- Code for Music File download -----------------  */

    private void startDownload(String url, String title) {
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Uri Download_uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(Download_uri);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(true);
        request.setTitle(title);
        request.setDescription("Downloading the music file you requested");
        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, title+".mp3");

        downloadReference = downloadManager.enqueue(request);

    }

    /*private void startDownload() {
        String url = "";
        //new DownloadF
    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... aurl) {
            int count=0;

            try {
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                int lengthOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Length of File: ");
            }
            catch (Exception e) {
                // ------------- Nothing here for now -------------//
            }

            return "Shubham";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }
    }*/
}
