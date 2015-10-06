package com.example.jerry.myodata;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Created by haven on 3/8/2015.
 */
public class ReadyActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ready_layout);
        ImageButton playButton;
        ImageButton homeButton;
        playButton = (ImageButton) findViewById(R.id.playButton);
        homeButton = (ImageButton) findViewById(R.id.homeButton);
        playButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick (View view){
                Intent intentP = new Intent (ReadyActivity.this,DataActivity.class);
                //football
                //pitch = 130
                //yaw = 50
                //roll = -160
                double[] data = {130.0,50.0,-160.0};
                intentP.putExtra("aim",data);
                startActivity(intentP);
            }
        });
        homeButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick (View view){
                Intent intentH = new Intent (ReadyActivity.this, MainActivity.class);
                startActivity(intentH);
            }
        });
    }


}
