package com.ISIMAFormation.tp1p1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Activity2 extends AppCompatActivity {
    /*                         3                         */
    public static final String EXTRA_MESSAGE = "NaPaD1Portanse";
    ItemAdapter _adapter = new ItemAdapter(null);;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        Intent intent = getIntent();
        String message = intent.getStringExtra(Activity2.EXTRA_MESSAGE);
        if(message!=null){
            TextView textView = findViewById(R.id.text2);
            textView.setText(message);
        }

        /*                        3.5                        */
        Button buttonGoBack = findViewById(R.id.buttonGoBack);
        buttonGoBack.setOnClickListener(view -> {
            Log.i("ButtonLaunch","MainActivity<---");
            finish();
        });

        /*                         4                         */
        Button buttonShare = findViewById(R.id.buttonShare);
        buttonShare.setOnClickListener(view -> {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(share, getString(R.string.ShareTitle)));
        });

        /*                         5                         */
        String uri = "https://perso.isima.fr/~aucatinon/jsonformation.json";
        new TakeJson().execute(uri);

        /*                         6                         */
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /*                         5                         */
    public static String streamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private class TakeJson extends AsyncTask<String, Integer, Boolean> {
        protected Boolean doInBackground(String... urls) {
            int count = urls.length;
            Boolean isW = true;
            URL url = null;
            int n = 3000;
            String Buffer;
            HttpURLConnection conn = null;
            try {
                url = new URL(urls[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000000);//5 seconds to download
                conn.setConnectTimeout(5000000);//5 seconds to connect
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                InputStream in = new BufferedInputStream(conn.getInputStream());
                Buffer = streamToString(in);
                Log.i("Valeur recup","\nn = "+ n + "\n" + Buffer + "\n");
                JSONObject json = new JSONObject(Buffer);
                JSONArray jsonArry = json.getJSONArray("Liste");
                for(int i=0;i<jsonArry.length();i++){
                    Log.i("Valeur recup", jsonArry.getString(i));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _adapter.chgJson(json);
                        _adapter.notifyDataSetChanged();
                    }
                });

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                isW = false;
            } finally {
                conn.disconnect();
            }
            Log.i("recup",isW.toString());
            return isW;
        }

        protected void onProgressUpdate(Integer... progress) {
            Log.i("Telechargement","progres : " + progress[0] + "%");
        }

    }

    /*                         6                         */
    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView _nameView;
        TextView _propertyView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            _nameView = itemView.findViewById(R.id.recycle_item_name);
            _propertyView = itemView.findViewById(R.id.recycle_item_property);
        }

        public void update(String name,String property){
            _nameView.setText(name);
            _propertyView.setText(property);
        }
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        private JSONObject _json;

        public ItemAdapter(JSONObject json) {
            _json = json;
        }

        public void chgJson(JSONObject json) {
            _json = json;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.recycle_item, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder viewHolder, int position) {
            try {
                JSONArray jsonArry = _json.getJSONArray("Liste");
                JSONObject json = jsonArry.getJSONObject(position);
                Log.i("Valeur recup", jsonArry.getString(position));
                viewHolder.update(json.getString("name"),json.getString("property"));
            } catch (JSONException e) {
                viewHolder.update("Unknown","Unknown");
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            int n = 0;
            if(_json == null){
                return 0;
            }
            try {
                JSONArray jsonArry = _json.getJSONArray("Liste");
                n = jsonArry.length();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return n;
        }
    }
}