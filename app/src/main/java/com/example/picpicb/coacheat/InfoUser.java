package com.example.picpicb.coacheat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info_user);

        Intent intent = getIntent();
        user = intent.getExtras().getParcelable("USER");

        nom = (TextView) findViewById(R.id.Nom);
        nom.setText(user.getPrenom()+" " + user.getNom());


        }
    }
