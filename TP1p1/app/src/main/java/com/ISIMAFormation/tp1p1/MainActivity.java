package com.ISIMAFormation.tp1p1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Bonjour
        /*                         1                         */
        Button buttonTest = findViewById(R.id.buttonTest);
        buttonTest.setOnClickListener(view -> {
            Log.i("ButtonTest","Bouton");
            Toast toast =  Toast.makeText(MainActivity.this, R.string.ToastButton, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
            toast.show();
        });

        /*                         2                         */
        TextView text = findViewById(R.id.text);
        EditText editText = findViewById(R.id.editText);
        editText.setOnKeyListener((v, keyCode, event) -> {
            Log.i("Action","Action = " + event.getAction());
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                Log.d("editText","editTextSeuil");
                runOnUiThread(() -> text.setText(editText.getText()));
                return true;
            }
            return false;
        });

        /*                         3                         */
        Button buttonLaunch = findViewById(R.id.buttonLaunch);
        buttonLaunch.setOnClickListener(view -> {
            Log.i("ButtonLaunch","---->Activity2");
            Intent intent = new Intent(this, Activity2.class);
            intent.putExtra(Activity2.EXTRA_MESSAGE,String.valueOf(editText.getText()));
            startActivity(intent);
        });
    }
}