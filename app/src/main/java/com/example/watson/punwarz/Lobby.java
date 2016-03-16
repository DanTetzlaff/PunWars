package com.example.watson.punwarz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

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
public class Lobby extends Page
{
    private final String LOBBY_ID = "LOBBY_ID";
    ListView list;
    CustomAdapter adapter;
    public  Lobby CustomListView = null;
    public  ArrayList<ListModel> CustomListViewValuesArr = new ArrayList<ListModel>();
    private boolean loadingMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        CustomListView = this;

        setListData();

        Resources res = getResources();
        list = ( ListView )findViewById( R.id.list );

        View footer = ( (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer, null, false);
        list.addFooterView(footer);

        adapter = new CustomAdapter( CustomListView, CustomListViewValuesArr,res);
        list.setAdapter( adapter );

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;

                if ((lastInScreen == totalItemCount) && !(loadingMore)){
                    Thread thread = new Thread(null, loadMoreListItems);
                    thread.start();
                }
            }
        });
    }

    public void setListData()
    {
        for (int i = 0; i < 5; i++){
            final ListModel sched = new ListModel();

            sched.setLobbyTitle("TITLE"+i);
            sched.setLobbyAuthor("By: "+i);
            sched.setExpireDate("12/31/2099 "+i);
            sched.setLobbyDes("Descrip "+i);
            sched.setTopPun("Best Pun "+i);
            sched.setLobbyID(123456);

            CustomListViewValuesArr.add( sched );
        }
    }

    public void onItemClick(int mPosition){
        ListModel tempValues = ( ListModel ) CustomListViewValuesArr.get(mPosition);

        Intent i = new Intent(Lobby.this, Puns.class);
        i.putExtra(LOBBY_ID, tempValues.getLobbyID());
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lobby, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

    if(id == R.id.logout_settings)
        {
            logOut(item);
            return true;
        }
        else if(id == R.id.profile_settings)
        {
            goToProfile(item);
            return true;
        }
        else if(id == R.id.addTheme_settings)
        {
            addAPrompt(item);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    //Never ending listView runnable #1 - initiation
    private Runnable loadMoreListItems = new Runnable() {
        @Override
        public void run() {
            loadingMore = true;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }

            runOnUiThread(returnRes);
        }
    };

    //Never ending listView runnable #2 - filling and updating listView
    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
            setListData();

            adapter.notifyDataSetChanged();

            loadingMore = false;
        }
    };

    public void goToProfile(MenuItem item)
    {
        Intent i = new Intent(Lobby.this, Profile.class);
        startActivity(i);
    }

    public void addAPrompt(MenuItem item)
    {
        Intent i = new Intent(Lobby.this, AddTitle.class);
        startActivity(i);
    }
}
