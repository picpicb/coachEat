package com.example.picpicb.coacheat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MenuJour extends AppCompatActivity {
    TextView t;
    TextView t2;
    TextView t3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_jour);

        t = (TextView) findViewById(R.id.textView9);
        t.setText("Menu Matin \n \nIngrédients:\n-sucre\n-riz\n \nEtapes:\n BlaBLABLABLA ");

        t2 = (TextView) findViewById(R.id.textView11);
        t2.setText("Menu Midi \n \nIngrédients:\n-sucre\n-riz\n \nEtapes:\n BlaBLABLABLA ");

        t3 = (TextView) findViewById(R.id.textView12);
        t3.setText("Menu Soir \n \nIngrédients:\n-sucre\n-riz\n \nEtapes:\n BlaBLABLABLA ");

    }
}
