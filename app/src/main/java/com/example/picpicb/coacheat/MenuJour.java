package com.example.picpicb.coacheat;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MenuJour extends AppCompatActivity {
    TextView t;
    TextView t2;
    TextView t3;
    recetteTask r ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_jour);
        r = new recetteTask();
        t = (TextView) findViewById(R.id.r1);
        t.setText("Menu Matin \n \nIngrédients:\n-sucre\n-riz\n \nEtapes:\n BlaBLABLABLA ");

        t2 = (TextView) findViewById(R.id.r2);
        t2.setText("Menu Midi \n \nIngrédients:\n-sucre\n-riz\n \nEtapes:\n BlaBLABLABLA ");

        t3 = (TextView) findViewById(R.id.r3);
        t3.setText("Menu Soir \n \nIngrédients:\n-sucre\n-riz\n \nEtapes:\n BlaBLABLABLA ");

        System.out.println( "---****************************");

        r.execute("800");
    }

        public class recetteTask extends AsyncTask<String, Void, String> {


            public void onPreExecute(){

            }
            @Override
            protected String doInBackground(String... Param){
                String r ="0";
                String str = "";
                try {
                    String url = "http://picpicb.ddns.net/api_coacheat/combi.php?cal=800"/*+ Param[0]*/;

                    URL object = new URL(url);

                    HttpURLConnection con = (HttpURLConnection) object.openConnection();

                    con.setRequestMethod("GET");

                    int reponseCode = con.getResponseCode();
                    System.out.println(reponseCode+" -------------------------");

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream())) ;
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while((inputLine =in.readLine() )!= null){
                        response.append(inputLine);
                    }

                    //in.close;

                    System.out.println(response.toString() + "****************************");

                    str = response.toString();


                    return str;
                }catch(IOException e) {
                    e.printStackTrace();
                } return str;
            }
            protected void onPostExecute(String response) {
                if (response != null) {
                    response = response.replace("[", "").replace("]", "");
                    recette r1 = null;
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        JSONArray array = new JSONArray( jsonObj.getString(""));
                        for(int i = 0; i<array.length();i++){
                            JSONObject obj = new JSONObject(array.getString(i));
                            r1 =new recette(obj.getString("listeIngredients"), obj.getString("nomRecette"),obj.getString("Etapes"),
                                    obj.getString("type"),obj.getString("nbrKal")
                                    );
                            if(i==0){
                                t.setText("Recette du: "+r1.getType() + r1.getNomR() + r1.getIngredients()+ r1.getEtape());
                            }if(i==1){
                                t2.setText("Recette du: "+r1.getType()+r1.getNomR() + r1.getIngredients()+ r1.getEtape());
                            }if(i==2){
                                t3.setText("Recette du: "+r1.getType()+r1.getNomR() + r1.getIngredients()+ r1.getEtape());
                            }

                        }
                        //recette = new recette();

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                }

            }
        }



}
