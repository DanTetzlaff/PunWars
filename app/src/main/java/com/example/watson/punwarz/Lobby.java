package com.example.watson.punwarz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

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
        FacebookSdk.sdkInitialize(getApplicationContext());
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(myToolbar);

        Button btn = (Button) findViewById(R.id.logout);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                logOut(v);
            }
        });
    }

    public void logOut(View v)
    {
        SharedPreferences sharedPref = getSharedPreferences("Toke_Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user_toke", "empty");
        editor.commit();

        LoginManager.getInstance().logOut();

        Intent i = new Intent(Lobby.this, Login.class);
        startActivity(i);
    }
}
