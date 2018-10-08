package com.example.sys.urlimage;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    private List <JobListItem> jobListItems;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        recyclerView = findViewById( R.id.recyclerView );

        recyclerView.setLayoutManager( new LinearLayoutManager( MainActivity.this ) );

        ArrayList <ChatBean> list = new ArrayList <>();

        jobListItems = new ArrayList <>();

        String url = "https://www.androidbegin.com/tutorial/jsonparsetutorial.txt";

        new ResponseAsync().execute( url );


    }


    private class ResponseAsync extends AsyncTask <String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog( MainActivity.this );
            progressDialog.setTitle( "Get response Tutorial" );
            progressDialog.setMessage( "Loading.." );
            progressDialog.setIndeterminate( false );
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String respURL = strings[0];

            try {
                // Download Image from URL

                URL url = new URL( respURL );

                InputStream is = url.openConnection().getInputStream();

                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader( new InputStreamReader( is ) );

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append( line + "\n" );
                }

                Log.e( "My data ", buffer.toString() );
                return buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            /*try {
                InputStream input = new URL( "flag" ).openStream();
                bitmap = BitmapFactory.decodeStream( input );
                input.close();
                File storagePath = Environment.getExternalStorageDirectory();
                OutputStream bytes = new FileOutputStream( new File( storagePath, "name.jpg" ));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                bytes.close();

            } catch (IOException e) {
                e.printStackTrace();
            }*/

            return null;


        }

        @Override
        protected void onPostExecute(String s) {
            Log.e( "Response data", s );
            try {

                JSONObject object = new JSONObject( s );
                JSONArray array = object.getJSONArray( "worldpopulation" );

                for (int i = 0; i < array.length(); i++) {

                    JobListItem item = new JobListItem();
                    JSONObject list_obj = array.getJSONObject( i );

                    item.setRank( list_obj.getString( "rank" ) );
                    item.setCountry( list_obj.getString( "country" ) );
                    item.setPopulation( list_obj.getString( "population" ) );
                    item.setFlag( list_obj.getString( "flag" ) );

//                    new Download().execute( list_obj.getString( "flag" ) );
                    jobListItems.add( item );


                }
                recyclerView.setAdapter( new WorldAdapter( MainActivity.this, jobListItems ) );
            } catch (JSONException e) {
                e.printStackTrace();
            }


            progressDialog.dismiss();
        }
    }




}


