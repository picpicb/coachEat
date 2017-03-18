package com.example.picpicb.coacheat;


import android.os.Parcel;
import android.os.Parcelable;
import android.os.AsyncTask;

public class Utilisateur implements Parcelable {
    private int id;
    private String nom;
    private String prenom;
    private String pseudo;
    private int age;
    private double poids;
    private int taille;
    private String objectif;

    public Utilisateur(int id, String nom, String prenom, String pseudo, int age, double poids, int taille, String objectif) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.pseudo = pseudo;
        this.age = age;
        this.poids = poids;
        this.taille = taille;
        this.objectif = objectif;
    }


    public static final Parcelable.Creator<Utilisateur> CREATOR = new Parcelable.Creator<Utilisateur>() {
        @Override
        public Utilisateur createFromParcel(Parcel source) {
            return new Utilisateur(source);
        }

        @Override
        public Utilisateur[] newArray(int size) {
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


    public String toString() {
        return "ID: " + id + " -- Prenom: " + prenom + " -- Nom: " + nom + " -- Pseudo: " + pseudo + " -- Age: " + age + " -- Poids: " + poids + " - Taille: " + taille;
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

}
