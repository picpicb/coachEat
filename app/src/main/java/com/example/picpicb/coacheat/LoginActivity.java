package com.example.picpicb.coacheat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import static com.example.picpicb.coacheat.R.id.password;

public class LoginActivity extends AppCompatActivity {
    private UserLoginTask connexionTask = null;
    private AutoCompleteTextView mailView;
    private EditText passwordView;
    private View mProgressView;
    private View mLoginFormView;
    private AppCompatActivity this2;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ****** Recuperation de ID si l'utilisateur s'est deja connecte ******
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int id = preferences.getInt("ID_UTILISATEUR",0);
        if(id != 0){
            connexionTask = new UserLoginTask("null", "null");
            connexionTask.execute(id);
        }

        // ****** Récuparation des variables, elements graphiques et parcelable ******
        mailView = (AutoCompleteTextView) findViewById(R.id.email);
        passwordView = (EditText) findViewById(password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        Button mEmailSignInButton = (Button) findViewById(R.id.ok);

        // ********************************* Setters *********************************
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        this2 = this;
    }


    private void attemptLogin() {
        if (connexionTask != null) {
            return;
        }
        mailView.setError(null);
        passwordView.setError(null);
        String email = mailView.getText().toString();
        String password = passwordView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        // Test mot de passe vide.
        if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;
        }
        // Test email vide.
        if (TextUtils.isEmpty(email)) {
            mailView.setError(getString(R.string.error_field_required));
            focusView = mailView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            connexionTask = new UserLoginTask(email, password);
            connexionTask.execute(0);
        }
    }


    private void showProgress(final boolean show) {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }


    // La connexion se fait en 2 etapes
    // 1) on envoie pseudo/mdp pour recuperer l'id utilisateur.
    // 2) avec cet id on recupère les infos utilisateur -> création object Utilisateur qui est parcelable

    public class UserLoginTask extends AsyncTask<Integer, Void, String> {
        private final String mEmail;
        private final String mPassword;
        private int id;

        public UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
            id = 0;
        }

        @Override
        protected String doInBackground(Integer... params) {
            String line = "0";

            // params[0] est l'id de l'utilisateur 0=jamais connecte
            //si l'id != 0 on recupere les infos utilisateur, sinon on se connecte avec mdp/pass pour recuperer l'id
            if(params[0]==0) {
                try {
                    URL url = new URL("http://picpicb.ddns.net/api_coacheat/login.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    String pparams = "pseudo=" + mEmail + "&pass=" + mPassword;
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(pparams);
                    writer.flush();
                    writer.close();
                    os.close();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    line = rd.readLine();
                    is.close();
                    rd.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                line = params[0].toString();
            }
            if(!line.equals("0")){
                id = Integer.parseInt(line);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.putInt("ID_UTILISATEUR",id);
                editor.commit();
                try {
                    URL url = new URL("http://picpicb.ddns.net/api_coacheat/coach.php?id="+id);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    line = rd.readLine();
                    StringBuilder sb = new StringBuilder();
                    sb.append(line);
                    is.close();
                    rd.close();
                    return sb.toString();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String reponse) {
            connexionTask = null;
            showProgress(false);
            if (reponse != null) {
                Utilisateur user = null;
                try {
                    JSONArray jar = new JSONArray(reponse);
                    JSONObject jsonObj = jar.getJSONObject(0);
                    user = new Utilisateur(id,jsonObj.getString("nom"),jsonObj.getString("prenom"),jsonObj.getString("pseudo"),jsonObj.getInt("age"),jsonObj.getDouble("poids"),jsonObj.getInt("taille"),jsonObj.getString("objectifEnCour"));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                Intent intent = new Intent(this2, MainActivity.class);
                intent.putExtra("USER", user);
                startActivity(intent);
            } else {
                passwordView.setError(getString(R.string.error_incorrect_password));
                passwordView.requestFocus();
            }
        }
        @Override
        protected void onCancelled() {
            connexionTask = null;
            showProgress(false);
        }
    }
}

