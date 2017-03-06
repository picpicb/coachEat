package com.example.picpicb.coacheat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class InfoUser extends AppCompatActivity {
    private Utilisateur user ;
    private TextView nom;
    private EditText age;
    private EditText poids;
    private EditText taille;
    private Spinner objectif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);

        // ****** Récuparation des variables, elements graphiques et parcelable ******
        Intent intent = getIntent();
        user = intent.getExtras().getParcelable("USER");
        nom = (TextView) findViewById(R.id.Nom);
        objectif = (Spinner) findViewById(R.id.objectif);
        age = (EditText) findViewById(R.id.age);
        poids = (EditText) findViewById(R.id.poids);
        taille = (EditText) findViewById(R.id.taille);
        Button ok = (Button) findViewById(R.id.button2);

        // ********************************* Setters *********************************
        nom.setText(user.getPrenom()+" " + user.getNom());
        age.setText(Integer.toString(user.getAge()));
        poids.setText(Double.toString( user.getPoids()));
        taille.setText(Integer.toString(user.getTaille()));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.objectifs, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        objectif.setAdapter(adapter);
        if (!user.getObjectif().equals(null)) {
            int spinnerPosition = adapter.getPosition(user.getObjectif());
            objectif.setSelection(spinnerPosition);
        }
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new UpdateDBTask().execute(1);
            }
        });

    }
    @Override
    public void onBackPressed() {

        //Sauvegarde apres modification de notre user dans l'activite principale.

        Intent data = new Intent();
        data.putExtra("USER",user);
        setResult(Activity.RESULT_OK, data);
        super.onBackPressed();
    }




    public class UpdateDBTask extends AsyncTask<Integer, Void, Boolean> {
        private ProgressDialog dialog = new ProgressDialog(InfoUser.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Chargement...");
            dialog.show();
            user.setObjectif(objectif.getSelectedItem().toString());
            user.setAge(Integer.parseInt(age.getText().toString()));
            user.setPoids(Double.parseDouble(poids.getText().toString()));
            user.setTaille(Integer.parseInt(taille.getText().toString()));
        }
        @Override
        protected Boolean doInBackground(Integer... params) {
                try {
                    Thread.sleep(1000);
                    URL url = new URL("http://picpicb.ddns.net/api_coacheat/updateUser.php?id="+user.getId()+"&age="+user.getAge()+"&poids="+user.getPoids()+"&taille="+user.getTaille()+"&objectif="+user.getObjectif());
                    System.out.println(url.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    int reponseCode = conn.getResponseCode();
                    conn.connect();
                    System.out.println("RESPONSE_CODE : " +reponseCode);
                    return true;
                }catch(IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean res) {
            dialog.dismiss();
            Toast toast = Toast.makeText(getApplicationContext(), "Données sauvegardées", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
