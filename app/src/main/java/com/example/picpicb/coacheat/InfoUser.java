package com.example.picpicb.coacheat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

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

        Intent intent = getIntent();
        user = intent.getExtras().getParcelable("USER");

        nom = (TextView) findViewById(R.id.Nom);
        nom.setText(user.getPrenom()+" " + user.getNom());
        objectif = (Spinner) findViewById(R.id.objectif);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.objectifs, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        objectif.setAdapter(adapter);
        if (!user.getObjectif().equals(null)) {
            int spinnerPosition = adapter.getPosition(user.getObjectif());
            objectif.setSelection(spinnerPosition);
        }
        age = (EditText) findViewById(R.id.age);
        poids = (EditText) findViewById(R.id.poids);
        taille = (EditText) findViewById(R.id.taille);
        age.setText(Integer.toString(user.getAge()));
        poids.setText(Double.toString( user.getPoids()));
        taille.setText(Integer.toString(user.getTaille()));


        }
    }
