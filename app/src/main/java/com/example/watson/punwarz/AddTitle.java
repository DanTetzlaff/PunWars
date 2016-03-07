package com.example.watson.punwarz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.lang.reflect.Array;
import java.util.Calendar;

/**
 * Author: Carille Mendoza
 * Created: March 2, 2016
 * Description: This class will handle adding new titles
 */
public class AddTitle extends AppCompatActivity
{
    private int mDay;
    private int mMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtitle);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        Spinner staticSpanner = (Spinner)findViewById(R.id.spin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        staticSpanner.setAdapter(adapter);
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

        sharedPref = getSharedPreferences("Prof_ID", MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putString("user_id", "empty");
        editor.commit();

        LoginManager.getInstance().logOut();

        Intent i = new Intent(AddTitle.this, Login.class);
        startActivity(i);
    }

    //collects data for prompt to be created
    public void submitPrompt(View v)
    {
        //
    }

    //returns to lobby page
    public void cancelEvent(View v)
    {
        Intent i = new Intent(AddTitle.this, Lobby.class);
        startActivity(i);
    }


}
