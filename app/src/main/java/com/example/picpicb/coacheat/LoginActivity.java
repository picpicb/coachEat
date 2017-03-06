package com.example.picpicb.coacheat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static android.Manifest.permission.READ_CONTACTS;
import static com.example.picpicb.coacheat.R.id.password;


public class LoginActivity extends AppCompatActivity {
    private UserLoginTask mAuthTask = null;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private AppCompatActivity this2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ****** Récuparation des variables, elements graphiques et parcelable ******
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(password);
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
        if (mAuthTask != null) {
            return;
        }
        mEmailView.setError(null);
        mPasswordView.setError(null);
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        // Test mot de passe vide.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        // Test email vide.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }


    private void showProgress(final boolean show) {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    public class UserLoginTask extends AsyncTask<Void, Void, String> {
        private final String mEmail;
        private final String mPassword;
        private int id;

        public UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
            id = 0;
        }

        @Override
        protected String doInBackground(Void... params) {
            String line = "0";
            try {
                URL url = new URL("http://picpicb.ddns.net/api_coacheat/login.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                String pparams = "pseudo="+mEmail+"&pass="+mPassword;
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

            if(!line.equals("0")){
                id = Integer.parseInt(line);
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
            mAuthTask = null;
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
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

