package com.example.picpicb.coacheat;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class MenuJour extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private AppCompatActivity this2;
    private String[] tabR;


    @Override
    protected  void onResume(){
        super.onResume();
        mSensorManager.registerListener(mSensorListener,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL );


    }
    @Override
    protected void onPause(){
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }



    private Utilisateur user ;
    TextView t;
    TextView t2;
    TextView t3;
    int nb ;
    double c;

    int i = 0;

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_jour);
        t = (TextView) findViewById(R.id.r1);
        t2 = (TextView) findViewById(R.id.r2);
        t3 = (TextView) findViewById(R.id.r3);


        Intent intent = getIntent();
        user = intent.getExtras().getParcelable("USER");



            c = 66.5 + (13.8 * user.getPoids())+(5. * ((double) user.getTaille())) -(6.8 * ((double) user.getAge()) );

            //On calcule le nombre de Kal dont il a besoin par jour
            //Si -Veut maigrir on propose des menus avec un apport de kalorie moindre
            //Si -Veut Grossir on propose plus
            //Si -Veut Maintenir on propose equivalent

        if((user.getObjectif()).equals("Maintenir")){
            nb = (int) c;
        }if((user.getObjectif()).equals("Maigrir")){
            nb = ((int) c )-300;
        }if((user.getObjectif()).equals("Grossir")){
            nb = ((int) c) +600;
        }

        new recetteTask().execute(nb+(int)(Math.random() * 150) );

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent =SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;





    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];

            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;

            if(mAccel > 15.){
                if(i>=12){i=0;}
                try {
                    currentThread().sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                t.setText(tabR[i]);
                t2.setText(tabR[i+1]);
                t3.setText(tabR[i+2]);i=i+3;

                if(i>=tabR.length){i=0;}

            }



        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    @Override
    public void onSensorChanged(SensorEvent event) {



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
                    tabR = new String[jsonar.length()];
                    Toast toast = Toast.makeText(getApplicationContext(), "Nombre de recettes chargées:"+(jsonar.length()/3)+"\n SECOUER pour Changer", Toast.LENGTH_LONG);
                    toast.show();
                    System.out.println(jsonar.length()+"  aaaaaaaaaaaaaaaaaa");
                    //On fait un array de tout le json = [ {recette1} , {recette2} , {recette3} ]
                    for(int i = 0; i<jsonar.length();i++){
                        JSONObject obj = new JSONObject(jsonar.getString(i));
                        //Au premier tour du for() on est sur la {recette 1} (un jsonObject)
                        System.out.println(obj.getString("nomRecette"));
                        //Pense à tester le type de recette pour savoir où la placer (matin,midi,soir)
                        if(obj.getString("type").equals(  "Matin" ) ){


                            t.setText(obj.getString("nomRecette")+"\n"+"\n"+obj.getString("listeIngredients")+"\n"+"\n"+ "\n"+"\n"+obj.getString("Etapes")+"\n"+"\n"+
                                   obj.getString("nbrKal"));
                            tabR[i]= obj.getString("nomRecette")+"\n"+"\n"+obj.getString("listeIngredients")+"\n"+"\n"+ "\n"+"\n"+obj.getString("Etapes")+"\n"+"\n"+
                                    obj.getString("nbrKal");

                        }if(obj.getString("type").equals(  "Midi" ) ){
                            t2.setText(obj.getString("nomRecette")+"\n"+"\n"+obj.getString("listeIngredients")+"\n"+"\n"+ "\n"+"\n"+obj.getString("Etapes")+
                                   "\n"+"\n"+obj.getString("nbrKal"));
                            tabR[i]= obj.getString("nomRecette")+"\n"+"\n"+obj.getString("listeIngredients")+"\n"+"\n"+ "\n"+"\n"+obj.getString("Etapes")+"\n"+"\n"+
                                    obj.getString("nbrKal");
                        }if(obj.getString("type").equals(  "Soir" )){
                            t3.setText(obj.getString("nomRecette")+"\n"+"\n"+obj.getString("listeIngredients")+"\n"+"\n"+ "\n"+"\n"+obj.getString("Etapes")+
                                   "\n"+"\n"+obj.getString("nbrKal")) ;
                            tabR[i]= obj.getString("nomRecette")+"\n"+"\n"+obj.getString("listeIngredients")+"\n"+"\n"+ "\n"+"\n"+obj.getString("Etapes")+"\n"+"\n"+
                                    obj.getString("nbrKal");
                        }
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}