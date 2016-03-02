package com.example.watson.punwarz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

/**
 * Author: Carille Mendoza
 * Created: March 2, 2016
 * Description: This class will handle adding new titles
 */
public class AddTitle extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puns);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
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

        if(id == R.id.lobby_settings)
        {
            goToLobby(item);
            return true;
        }
        else if(id == R.id.logout_settings)
        {
            logOut(item);
            return true;
        }
        else if(id == R.id.profile_settings)
        {
            goToProfile(item);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void goToLobby(MenuItem item)
    {
        Intent i = new Intent(AddTitle.this, Lobby.class);
        startActivity(i);
    }

    public void goToProfile(MenuItem item)
    {

    }

    public void logOut(MenuItem item)
    {
        SharedPreferences sharedPref = getSharedPreferences("Toke_Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user_toke", "empty");
        editor.commit();

        LoginManager.getInstance().logOut();

        Intent i = new Intent(AddTitle.this, Login.class);
        startActivity(i);
    }

}
