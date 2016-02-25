package com.example.watson.punwarz;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toolbar;

/**
 * Author: Carille Mendoza
 * Created: 2016-02-24
 * Description: This class will handle the Lobby page of the app where all the themes will exist
 */
public class Lobby extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(myToolbar);
    }
}
