package com.example.picpicb.coacheat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class InfoUser extends AppCompatActivity {
    private Utilisateur user ;
    private TextView v ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info_user);

        Intent intent = getIntent();
        user = intent.getExtras().getParcelable("USER");

        System.out.println("---------------------------------");
        System.out.println(user.toString());

        v = (TextView) findViewById(R.id.info1);
        v.setText(user.toString()); /*"\n" +*/

        ;


    }
}
