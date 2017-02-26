package com.example.picpicb.coacheat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import org.w3c.dom.Text;

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
    ImageView nutriscore;
    TextView text;
    Button bscan;

    public final static int MY_PERMISSIONS_PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        bscan = (Button) findViewById(R.id.bscan);
        text = (TextView) findViewById(R.id.textView7);
        final AppCompatActivity this2 = this;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.CAMERA},MY_PERMISSIONS_PERMISSIONS_REQUEST);
        }
        bscan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                text.setVisibility(View.INVISIBLE);
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
        nutriscore = (ImageView) findViewById(R.id.nutriscore);



    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    new IntentIntegrator(this).initiateScan();

                } else {

                    Toast toast = Toast.makeText(getApplicationContext(), "Vous n'avez pas autorisé les permissions", Toast.LENGTH_SHORT);
                    toast.show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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
                photoProduit.setVisibility(View.VISIBLE);
                switch (produit.getString("nutrition_grades")) {
                    case "a":
                        nutriscore.setImageResource(R.drawable.nutriscorea);
                        break;
                    case "b":
                        nutriscore.setImageResource(R.drawable.nutriscoreb);
                        break;
                    case "c":
                        nutriscore.setImageResource(R.drawable.nutriscorec);
                        break;
                    case "d":
                        nutriscore.setImageResource(R.drawable.nutriscored);
                        break;
                    case "e":
                        nutriscore.setImageResource(R.drawable.nutriscoree);
                        break;
                    default: break;
                }

                nutriscore.setVisibility(View.VISIBLE);
                nomProduit.setText(produit.getString("product_name_fr"));
                nomProduit.setVisibility(View.VISIBLE);

                calories.setText("Energie: "+nutriments.getString("energy")+" kJ");
                calories.setVisibility(View.VISIBLE);
                sel.setText("Sel: "+nutriments.getString("salt_100g")+"g");
                sel.setVisibility(View.VISIBLE);
                lipides.setText("Lipides: "+nutriments.getString("fat_100g")+"g");
                lipides.setVisibility(View.VISIBLE);
                glucides.setText("Glucides: "+nutriments.getString("carbohydrates_100g")+"g");
                glucides.setVisibility(View.VISIBLE);
                proteines.setText("Protéines: "+nutriments.getString("proteins_100g")+"g");
                proteines.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                photoProduit.setVisibility(View.INVISIBLE);
                nomProduit.setVisibility(View.INVISIBLE);
                nutriscore.setVisibility(View.INVISIBLE);
                calories.setVisibility(View.INVISIBLE);
                sel.setVisibility(View.INVISIBLE);
                lipides.setVisibility(View.INVISIBLE);
                glucides.setVisibility(View.INVISIBLE);
                proteines.setVisibility(View.INVISIBLE);
                text.setVisibility(View.VISIBLE);
                Toast toast = Toast.makeText(getApplicationContext(), "Produit inconnu", Toast.LENGTH_SHORT);
                toast.show();

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
