package com.example.watson.punwarz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Menu;
import android.view.MenuInflater;

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
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        Button btn = (Button) findViewById(R.id.logout);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                logOut(v);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.action_settings)
        {
            return true;
        }
        if(id == R.id.logout_settings)
        {
            //logOut(v);
        }

        return super.onOptionsItemSelected(item);
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
