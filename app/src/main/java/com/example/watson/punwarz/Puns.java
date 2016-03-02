package com.example.watson.punwarz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

import com.example.watson.punwarz.ListView.PunAdapter;
import com.example.watson.punwarz.ListView.PunModel;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.util.ArrayList;

/**
 * Author:  Daniel Tetzlaff
 * Created: 2016-03-01
 */
public class Puns extends AppCompatActivity
{
    ListView list;
    PunAdapter adapter;
    public  Puns CustomListView = null;
    public  ArrayList<PunModel> CustomListViewValuesArr = new ArrayList<>();
    private int lobbyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puns);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

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
        for (int i = 0; i < 5; i++){
            final PunModel sched = new PunModel();

            sched.setPunAuth("By: " + i);
            sched.setPun("THE PUN " + i);
            sched.setPunVotes("num " + i + lobbyID);

            CustomListViewValuesArr.add( sched );
        }
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
        Intent i = new Intent(Puns.this, Lobby.class);
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

        Intent i = new Intent(Puns.this, Login.class);
        startActivity(i);
    }
}
