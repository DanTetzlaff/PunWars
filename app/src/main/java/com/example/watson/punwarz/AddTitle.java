package com.example.watson.punwarz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.FacebookSdk;

import java.util.Calendar;
import java.util.Date;

/**
 * Author: Carille Mendoza
 * Created: March 2, 2016
 * Modified: March 16, 2016
 * Desc: Can now add themes to the lobby page
 */
public class AddTitle extends Page
{
    private int lobbyID;

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

        lobbyID = this.getIntent().getIntExtra("LOBBY_ID", 0);
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

    public void goToProfile(MenuItem item)
    {
        Intent i = new Intent(AddTitle.this, Profile.class);
        startActivity(i);
    }

    //collects data for prompt to be created
    public void submitPrompt(View v)
    {
        //gets theme
        final EditText promptField = (EditText) findViewById(R.id.promptField);
        String prompt = promptField.getText().toString().toLowerCase();
        //gets description
        final EditText promptDescField = (EditText) findViewById(R.id.promptDescField);
        String promptDesc = promptDescField.getText().toString();
        //gets length of pun validity
        final Spinner dateSpinner = (Spinner) findViewById(R.id.spin);
        int length = Integer.valueOf((String) dateSpinner.getSelectedItem());

        //computes for expiry based on date creation
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, length);
        Date expDate = cal.getTime();

        ParseApplication parse = new ParseApplication();
        com.facebook.Profile prof = com.facebook.Profile.getCurrentProfile();

        boolean exists = parse.doesThemeExists(prompt);
        if(!exists)
        {
            parse.createNewLobby(prof.getId(), prompt, promptDesc, expDate);
            Toast.makeText(getApplicationContext(), "Theme added Successfully!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(AddTitle.this, Lobby.class);
            i.putExtra("LOBBY_ID", lobbyID);

            startActivity(i);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Theme already exists!", Toast.LENGTH_SHORT).show();
            destroyKeyboard();
        }
    }

    //returns to lobby page
    public void cancelEvent(View v)
    {
        Intent i = new Intent(AddTitle.this, Lobby.class);
        startActivity(i);
    }

    public void destroyKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
