package com.example.picpicb.coacheat;

import java.io.BufferedWriter;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
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
import java.net.MalformedURLException;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by JM on 23/02/2017.
 */

public class Utilisateur {
    private int id;
    private String nom;
    private String prenom;
    private String pseudo;
    private int age;
    private  int poids;
    private int taille;
    private String objectif;
    private String photo;

    public Utilisateur(int id){
        this.id = id;
        //recuperation des données depuis la BD
        (new UserInfoTask("https://picpicb.ddns.net/api_coacheat/coach.php?id="+this.id)).execute((Void) null);
    }

    class UserInfoTask extends AsyncTask<Void, Void, String> {
        String url_info;
        public UserInfoTask(String url) {
            url_info = url;
        }

        @Override
        protected String doInBackground(Void... params) {
            String line = "0";
            try {
                URL url = new URL(url_info);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                SSLContext ctx1 = SSLContext.getInstance("TLS");
                ctx1.init(null, new TrustManager[] {
                        new X509TrustManager() {
                            public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                            public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                            public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
                        }
                }, null);
                conn.setDefaultSSLSocketFactory(ctx1.getSocketFactory());
                conn.setDefaultHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                String pparams = "" ;
                OutputStream os =null;
                os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(pparams);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                line = rd.readLine();
                StringBuilder sb = new StringBuilder();
                sb.append(line);
                is.close();
                rd.close();
                return sb.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String reponse) {
            reponse = reponse.replace("[", "").replace("]", "");
            try {
                JSONObject jsonObj = new JSONObject(reponse);
                setNom(jsonObj.getString("nom"));
                setPrenom(jsonObj.getString("nom"));
                setPseudo(jsonObj.getString("pseudo"));
                setAge(jsonObj.getInt("age"));
                setTaille(jsonObj.getInt("taille"));
                setPoids(jsonObj.getInt("poids"));
                setPhoto(jsonObj.getString("photoLien"));
                setObjectif(jsonObj.getString("objectifEnCour"));
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }
    }



    public String toString(){
        return "ID: "+id+" -- Prenom: "+prenom+" -- Nom: "+nom+" -- Pseudo: "+pseudo+" -- Age: "+age+" -- Poids: "+poids+" - Taille: "+taille;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getPoids() {
        return poids;
    }

    public void setPoids(int poids) {
        this.poids = poids;
    }

    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

    public String getObjectif() {
        return objectif;
    }

    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
