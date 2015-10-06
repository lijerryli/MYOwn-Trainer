package com.example.jerry.myodata;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;


public class ResultsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);



        ImageButton restartButton = (ImageButton) findViewById(R.id.restartButton);
        ImageButton homeButton = (ImageButton) findViewById(R.id.homeButton);
        restartButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick (View view){
                Intent intentR = new Intent (ResultsActivity.this, ReadyActivity.class);
                startActivity(intentR);
            }
        });
        homeButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick (View view){
                Intent intentH = new Intent (ResultsActivity.this, MainActivity.class);
                startActivity(intentH);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
