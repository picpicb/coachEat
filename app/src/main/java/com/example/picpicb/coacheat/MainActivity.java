package com.example.picpicb.coacheat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    public ArrayList<ToggleButton> toggles;
    private AppCompatActivity this2;
    private Utilisateur user;
    private TextView m1,m2,m3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        user = intent.getExtras().getParcelable("USER");
        m1 = (TextView) findViewById(R.id.m1);
        m2 = (TextView) findViewById(R.id.m2);
        m3 = (TextView) findViewById(R.id.m3);
        toggles = new ArrayList<ToggleButton>();
        toggles.add((ToggleButton) findViewById(R.id.toggleButton));
        toggles.add((ToggleButton) findViewById(R.id.toggleButton2));
        toggles.add((ToggleButton) findViewById(R.id.toggleButton3));
        this2 = this;

        Drawable drawable = getResources().getDrawable(R.drawable.fouchette);
        int bound = (int) (drawable.getIntrinsicWidth() * 0.5);
        drawable.setBounds(0, 0, bound, bound);
        m1.setCompoundDrawables(drawable, null, null, null);

        drawable = getResources().getDrawable(R.drawable.accountlogo);
        bound = (int) (drawable.getIntrinsicWidth() * 0.5);
        drawable.setBounds(0, 0, bound, bound);
        m2.setCompoundDrawables(drawable, null, null, null);

        drawable = getResources().getDrawable(R.drawable.codebarre);
        bound = (int) (drawable.getIntrinsicWidth() * 0.5);
        drawable.setBounds(0, 0, bound, bound);
        m3.setCompoundDrawables(drawable, null, null, null);




        for(ToggleButton t : toggles){
            t.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    for (ToggleButton t : toggles) {
                        if(t.getId() != v.getId())
                            t.setChecked(false);
                    }
                }
            });
        }



        m1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(this2, MenuJour.class);
                intent.putExtra("USER",user);
                startActivity(intent);
            }
        });
        m2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(this2, InfoUser.class);
                intent.putExtra("USER",user);
                startActivityForResult(intent, 1);
            }
        });
        m3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(this2, Scan.class);
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
