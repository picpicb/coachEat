package com.example.picpicb.coacheat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Scan extends AppCompatActivity {
    TextView calories;
    TextView sel;
    TextView lipides;
    TextView glucides;
    TextView proteines;
    TextView nomProduit;
    ImageView photoProduit;

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
        calories = (TextView) findViewById(R.id.textView);
        sel = (TextView) findViewById(R.id.textView6);
        lipides = (TextView) findViewById(R.id.textView3);
        glucides = (TextView) findViewById(R.id.textView4);
        proteines = (TextView) findViewById(R.id.textView5);
        nomProduit = (TextView) findViewById(R.id.textView2);
        photoProduit = (ImageView) findViewById(R.id.imageView);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent ){
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            // nous récupérons le contenu du code barre
            String scanContent = scanningResult.getContents();
            OpenFood opt= new OpenFood(scanContent);
            opt.execute((Void) null);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),"Aucune donnée reçu!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    class OpenFood extends AsyncTask<Void, Void, String> {
        private String codeB;

        public OpenFood(String scanContent) {
            codeB = scanContent;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("https://fr.openfoodfacts.org/api/v0/produit/"+codeB+".json");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                int reponse = conn.getResponseCode();
                InputStream is = conn.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                String line = "";
                StringBuilder sb = new StringBuilder();
                while((line = r.readLine()) != null){
                    sb.append(line+"\n");
                }
                is.close();
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String reponse){
            try {
                JSONObject jrep = new JSONObject(reponse);
                JSONObject produit = jrep.getJSONObject("product");
                JSONObject nutriments = produit.getJSONObject("nutriments");

                new DownloadImageTask(photoProduit).execute(produit.getString("image_front_url"));
                nomProduit.setText(produit.getString("product_name_fr"));

                calories.setText(nutriments.getString("energy"));
                sel.setText(nutriments.getString("salt_100g"));
                lipides.setText(nutriments.getString("fat_100g"));
                glucides.setText(nutriments.getString("carbohydrates_100g"));
                proteines.setText(nutriments.getString("proteins_100g"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
