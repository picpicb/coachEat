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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_jour);
        t = (TextView) findViewById(R.id.r1);
        //t.setText("Menu Matin \n \nIngrédients:\n-sucre\n-riz\n \nEtapes:\n BlaBLABLABLA ");

        t2 = (TextView) findViewById(R.id.r2);
        //t2.setText("Menu Midi \n \nIngrédients:\n-sucre\n-riz\n \nEtapes:\n BlaBLABLABLA ");

        t3 = (TextView) findViewById(R.id.r3);
        //t3.setText("Menu Soir \n \nIngrédients:\n-sucre\n-riz\n \nEtapes:\n BlaBLABLABLA ");

        System.out.println( "---****************************");

        new recetteTask().execute(800);
    }

        public class recetteTask extends AsyncTask<Integer, Void, String> {
            String str="";

            @Override
            protected String doInBackground(Integer... Param){
                try {
                    String url = "http://picpicb.ddns.net/api_coacheat/combi.php?cal="+Param[0];
                    URL object = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) object.openConnection();
                    con.setRequestMethod("GET");
                    int reponseCode = con.getResponseCode();
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())) ;
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while((inputLine =in.readLine() )!= null){
                        response.append(inputLine);
                    }
                    //in.close;
                    str = response.toString();
                    return str;
                }catch(IOException e) {
                    e.printStackTrace();
                } return str;
            }

            protected void onPostExecute(String response) {
                if (response != null) {
                    try {
                        JSONArray jsonar = new JSONArray(response);
                        //On fait un array de tout le json = [ {recette1} , {recette2} , {recette3} ]
                        for(int i = 0; i<jsonar.length();i++){

                            JSONObject obj = new JSONObject(jsonar.getString(i));
                            //Au premier tour du for() on est sur la {recette 1} (un jsonObject)

                            System.out.println(obj.getString("nomRecette"));
                            //Pense à tester le type de recette pour savoir où la placer (matin,midi,soir)




                            if(obj.getString("type").equals(  "Matin" ) ){
                                t.setText("Recette du: "+obj.getString("type") +"\n"+obj.getString("listeIngredients")+"\n"+ obj.getString("nomRecette")+"\n"+obj.getString("Etapes")+"\n"+
                                        obj.getString("nbrKal"));
                            }if(obj.getString("type").equals(  "Midi" ) ){
                                t2.setText("Recette du: "+obj.getString("type") +"\n"+obj.getString("listeIngredients")+"\n"+ obj.getString("nomRecette")+"\n"+obj.getString("Etapes")+
                                        "\n"+obj.getString("nbrKal"));
                            }if(obj.getString("type").equals(  "Soir" )){
                                t3.setText("Recette du: "+obj.getString("type") +"\n"+obj.getString("listeIngredients")+"\n"+ obj.getString("nomRecette")+"\n"+obj.getString("Etapes")+
                                        "\n"+obj.getString("nbrKal"));
                            }
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                }

            }
        }



}
