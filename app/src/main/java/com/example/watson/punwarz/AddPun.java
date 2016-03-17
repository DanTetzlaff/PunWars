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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
    private String lobbyID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpun);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        lobbyID = this.getIntent().getStringExtra("LOBBY_ID");
    }

    public void submitPun(View v)
    {
        ParseApplication parse = new ParseApplication();
        Profile profile = Profile.getCurrentProfile();

        EditText punText = (EditText)findViewById(R.id.editText);

        parse.createNewPun(profile.getId(), lobbyID, punText.getText().toString());

        Toast.makeText(getApplicationContext(), "Pun added Successfully!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(AddPun.this, Puns.class);
        i.putExtra("LOBBY_ID", lobbyID);

        destroyKeyboard();
        startActivity(i);
    }

    public void cancelEvent(View v)
    {
        Intent i = new Intent(AddPun.this, Lobby.class);
        destroyKeyboard();
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
