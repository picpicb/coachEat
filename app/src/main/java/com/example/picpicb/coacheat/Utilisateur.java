package com.example.picpicb.coacheat;

import java.io.BufferedWriter;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.Parcelable;
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

public class Utilisateur implements Parcelable {
    private int id;
    private String nom;
    private String prenom;
    private String pseudo;
    private int age;
    private double poids;
    private int taille;
    private String objectif;
    private String photo;

    public Utilisateur(int id, String nom, String prenom, String pseudo, int age, double poids, int taille, String objectif, String photo) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.pseudo = pseudo;
        this.age = age;
        this.poids = poids;
        this.taille = taille;
        this.objectif = objectif;
        this.photo = photo;
    }


    public static final Parcelable.Creator<Utilisateur> CREATOR = new Parcelable.Creator<Utilisateur>()
    {
        @Override
        public Utilisateur createFromParcel(Parcel source)
        {
            return new Utilisateur(source);
        }

        @Override
        public Utilisateur[] newArray(int size)
        {
            return new Utilisateur[size];
        }
    };
    public Utilisateur(Parcel in) {
        this.id = in.readInt();
        this.nom = in.readString();
        this.prenom = in.readString();
        this.pseudo = in.readString();
        this.age = in.readInt();
        this.poids = in.readDouble();
        this.taille = in.readInt();
        this.objectif = in.readString();
        this.photo = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nom);
        dest.writeString(prenom);
        dest.writeString(pseudo);
        dest.writeInt(age);
        dest.writeDouble(poids);
        dest.writeInt(taille);
        dest.writeString(objectif);
        dest.writeString(photo);
    }


    class UserInfoTask extends AsyncTask<Void, Void, String> {
        String url_info;
        public UserInfoTask(String url) {
            url_info = url;
        }

        @Override
        protected String doInBackground(Void... params) {
            String line = "0";

            return null;
        }

        @Override
        protected void onPostExecute(String reponse) {


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

    public double getPoids() {
        return poids;
    }

    public void setPoids(double poids) {
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
