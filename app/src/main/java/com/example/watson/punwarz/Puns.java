package com.example.watson.punwarz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import com.example.watson.punwarz.ListView.PunAdapter;
import com.example.watson.punwarz.ListView.PunModel;
import com.example.watson.punwarz.AddPun;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.parse.Parse;

import java.util.ArrayList;

/**
 * Author:  Daniel Tetzlaff
 * Created: 2016-03-01
 * Description: This class takes care of the displaying and managing the puns
 */
public class Puns extends Page
{
    private final String LOBBY_ID = "LOBBY_ID";
    ListView list;
    PunAdapter adapter;
    public  Puns CustomListView = null;
    public  ArrayList<PunModel> CustomListViewValuesArr = new ArrayList<>();
    private int lobbyID;
    private ParseApplication parse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puns);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        parse = new ParseApplication();

        Intent intent = getIntent();
        lobbyID = intent.getIntExtra("LOBBY_ID", 0);

        CustomListView = this;

        setListData();

        Resources res = getResources();
        list = ( ListView )findViewById( R.id.list );

        adapter = new PunAdapter( CustomListView, CustomListViewValuesArr,res);
        list.setAdapter( adapter );
    }

    public void setListData()
    {
        ArrayList<ArrayList<String>> puns = parse.getPuns(Integer.toString(lobbyID));


        for (int i = 0; i < puns.size(); i++){
            ArrayList<String> current = puns.get(i);
            final PunModel sched = new PunModel();

                sched.setPunAuth("By: " + current.get(1));
                sched.setPun(current.get(0));
                sched.setPunVotes("num " + i + lobbyID);

            CustomListViewValuesArr.add(sched);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_puns, menu);
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
        else if(id == R.id.addPun_settings)
        {
            addAPun(item);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToProfile(MenuItem item)
    {
        Intent i = new Intent(Puns.this, Profile.class);
        startActivity(i);
    }

    public void addAPun(MenuItem item)
    {
        Intent i = new Intent(Puns.this, AddPun.class);
        i.putExtra(LOBBY_ID, lobbyID);
        startActivity(i);
    }
}
