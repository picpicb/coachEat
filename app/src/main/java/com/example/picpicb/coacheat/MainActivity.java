package com.example.picpicb.coacheat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private AppCompatActivity this2;
    private Utilisateur user;
    private TextView menuButton[];
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user = intent.getExtras().getParcelable("USER");
        menuButton = new TextView[3];
        menuButton[0] = (TextView) findViewById(R.id.m1);
        menuButton[1] = (TextView) findViewById(R.id.m2);
        menuButton[2] = (TextView) findViewById(R.id.m3);
        FloatingActionButton deconnexion = (FloatingActionButton) findViewById(R.id.deconnexion);
        this2 = this;

        Drawable drawable = getResources().getDrawable(R.drawable.fouchette);
        int bound = (int) (drawable.getIntrinsicWidth() * 0.5);
        drawable.setBounds(0, 0, bound, bound);
        menuButton[0].setCompoundDrawables(drawable, null, null, null);
        drawable = getResources().getDrawable(R.drawable.accountlogo);
        bound = (int) (drawable.getIntrinsicWidth() * 0.5);
        drawable.setBounds(0, 0, bound, bound);
        menuButton[1].setCompoundDrawables(drawable, null, null, null);
        drawable = getResources().getDrawable(R.drawable.codebarre);
        bound = (int) (drawable.getIntrinsicWidth() * 0.5);
        drawable.setBounds(0, 0, bound, bound);
        menuButton[2].setCompoundDrawables(drawable, null, null, null);

        for(int i=0; i<3; i++){
            menuButton[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            v.setBackgroundColor(Color.LTGRAY);
                            break;
                        case MotionEvent.ACTION_UP:
                            v.setBackgroundColor(Color.WHITE);
                            break;
                    }
                    return false;
                }
            });
        }

        menuButton[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(this2, MenuJour.class);
                intent.putExtra("USER",user);
                startActivity(intent);
            }
        });
        menuButton[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(this2, InfoUser.class);
                intent.putExtra("USER",user);
                startActivityForResult(intent, 1);
            }
        });
        menuButton[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(this2, Scan.class);
                startActivity(intent);
            }
        });
        deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.putInt("ID_UTILISATEUR",0);
                editor.commit();
                Intent intent = new Intent(this2, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            user = data.getExtras().getParcelable("USER");
        }
    }

}
