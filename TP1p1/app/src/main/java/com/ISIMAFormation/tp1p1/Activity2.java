package com.ISIMAFormation.tp1p1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


public class Activity2 extends AppCompatActivity {
    /*                         3                         */
    public static final String EXTRA_MESSAGE = "NaPaD1Portanse";
    ItemAdapter _adapter = new ItemAdapter(null);

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
        Button buttonGoBack = findViewById(R.id.buttonWebview);
        buttonGoBack.setOnClickListener(view -> {
            Intent intentWebview = new Intent(this, WebViewActivity.class);
            startActivity(intentWebview);
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
        new Thread(() -> getJSON(uri)).start();

        /*                         6                         */
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /*                         5                         */
    public static String streamToString(InputStream i) {
        Scanner s = new Scanner(i).useDelimiter("\\a");
        return s.hasNext() ? s.next() : "";
    }

    void getJSON(String urlstr) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlstr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000000);
            conn.setConnectTimeout(5000000);
            conn.setRequestMethod("GET");
            conn.connect();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            JSONObject json = new JSONObject(streamToString(in));
            runOnUiThread(() -> {
                _adapter.chgJson(json);//6
                _adapter.notifyDataSetChanged();//6
            });
        } catch (IOException | JSONException e) {}
        finally { if(conn!=null)  conn.disconnect(); }
    }

    /*                         6                         */
    static class ItemViewHolder extends RecyclerView.ViewHolder {

        final TextView _nameView;
        final TextView _propertyView;

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

    static class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        private JSONObject _json;

        public ItemAdapter(JSONObject json) { _json = json; }

        public void chgJson(JSONObject json) { _json = json; }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.recycle_item, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder viewHolder, int position) {
            try {
                JSONArray jsonArry = _json.getJSONArray("Liste");
                JSONObject json = jsonArry.getJSONObject(position);
                viewHolder.update(json.getString("name"),json.getString("property"));
            } catch (JSONException e) {
                viewHolder.update("Unknown","Unknown");
                Log.e("Valeur recup","a pas trouvé l'element");
            }
        }

        @Override
        public int getItemCount() {
            if(_json == null)   return 0;
            int n = 0;
            try {
                JSONArray jsonArry = _json.getJSONArray("Liste");
                n = jsonArry.length();
            } catch (JSONException e) {}
            return n;
        }
    }
}