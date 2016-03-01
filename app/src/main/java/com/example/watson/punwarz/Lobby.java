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
import android.widget.Toast;

import com.example.watson.punwarz.ListView.CustomAdapter;
import com.example.watson.punwarz.ListView.ListModel;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.util.ArrayList;

/**
 * Author: Carille Mendoza
 * Created: 2016-02-24
 * Description: This class will handle the Lobby page of the app where all the themes will exist
 */
public class Lobby extends AppCompatActivity
{
    ListView list;
    CustomAdapter adapter;
    public  Lobby CustomListView = null;
    public  ArrayList<ListModel> CustomListViewValuesArr = new ArrayList<ListModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        CustomListView = this;

        setListData();

        Resources res = getResources();
        list = ( ListView )findViewById( R.id.list );

        adapter = new CustomAdapter( CustomListView, CustomListViewValuesArr,res);
        list.setAdapter( adapter );
    }

    public void setListData()
    {
        for (int i = 0; i < 10; i++){
            final ListModel sched = new ListModel();

            sched.setLobbyTitle("TITLE"+i);
            sched.setLobbyAuthor("By: "+i);
            sched.setExpireDate("12/31/2099 "+i);
            sched.setLobbyTheme("Pun "+i);
            sched.setTopPun("Best Pun "+i);

            CustomListViewValuesArr.add( sched );
        }
    }

    public void onItemClick(int mPosition){
        ListModel tempValues = ( ListModel ) CustomListViewValuesArr.get(mPosition);

        Toast.makeText(CustomListView,
                ""+tempValues.getLobbyTitle()+" "+tempValues.getLobbyTheme(), Toast.LENGTH_LONG).show();
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

        Intent i = new Intent(Lobby.this, Login.class);
        startActivity(i);
    }
}
