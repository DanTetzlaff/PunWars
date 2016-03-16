package com.example.watson.punwarz;

import android.content.Context;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.*;
import com.facebook.Profile;
import com.facebook.login.LoginManager;

/**
 * Author: Carille
 * Created: 2016-03-06
 * Desc: Handles adding new puns to the system
 */
public class AddPun extends AddTitle
{
    private int lobbyID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpun);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        lobbyID = this.getIntent().getIntExtra("LOBBY_ID", 0);
    }

    public void submitPun(View v)
    {
        ParseApplication parse = new ParseApplication();
        Profile profile = Profile.getCurrentProfile();

        parse.createNewPun(profile.getId(), Integer.toString(lobbyID), "TEST DATA");

        Toast.makeText(getApplicationContext(), "Pun added Successfully!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(AddPun.this, Puns.class);
        i.putExtra("LOBBY_ID", lobbyID);
        startActivity(i);
    }

    public void cancelEvent(View v)
    {
        Intent i = new Intent(AddPun.this, Lobby.class);
        startActivity(i);
    }
}
