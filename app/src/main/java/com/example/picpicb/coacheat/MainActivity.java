package com.example.picpicb.coacheat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    public ArrayList<ToggleButton> toggles;
    public ListView menu;
    public ImageButton userImage;
    private int USER_ID;
    private AppCompatActivity this2;
    private Utilisateur user;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        user = intent.getExtras().getParcelable("USER");
        toggles = new ArrayList<ToggleButton>();
        toggles.add((ToggleButton) findViewById(R.id.toggleButton));
        toggles.add((ToggleButton) findViewById(R.id.toggleButton2));
        toggles.add((ToggleButton) findViewById(R.id.toggleButton3));
        menu = (ListView) findViewById(R.id.menu);
        userImage = (ImageButton) findViewById(R.id.userImage);
        this2 = this;
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

        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                Intent intent = new Intent(this2, Scan.class);
                startActivity(intent);
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        System.out.println("///////////////");
        System.out.println(user);
    }

}
