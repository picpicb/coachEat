package com.example.picpicb.coacheat;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Scan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Button bscan = (Button) findViewById(R.id.bscan);
        final AppCompatActivity this2 = this;
        bscan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new IntentIntegrator(this2).initiateScan();
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent ){
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {

            // nous récupérons le contenu du code barre
            String scanContent = scanningResult.getContents();

            // nous récupérons le format du code barre
            String scanFormat = scanningResult.getFormatName();

            System.out.println(scanContent);

            OpenFood opt= new OpenFood(scanContent);
            opt.execute((Void) null);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Aucune donnée reçu!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }
    public class OpenFood extends AsyncTask<Void, Void, Boolean> {
        private int codeB;

        public OpenFood(String scanContent) {
            codeB = Integer.parseInt(scanContent);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL("https://fr.openfoodfacts.org/api/v0/produit/"+codeB+".json");

                urlConnection = (HttpURLConnection) url
                        .openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader isw = new InputStreamReader(in);

                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    System.out.print(current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

        }
    }
}
