package watson.punwarz;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import watson.punwarz.ListView.CustomAdapter;
import watson.punwarz.ListView.ListModel;
import com.facebook.FacebookSdk;

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
    View noItemFooter;  // holder for "no more items" footer
    View footer;        // holder for "loading more items" footer
    CustomAdapter adapter;
    boolean noMore = false;

    public  Lobby CustomListView = null;
    public  ArrayList<ListModel> CustomListViewValuesArr = new ArrayList<ListModel>();
    private boolean loadingMore = false;
    private ParseApplication parse = new ParseApplication();
    public SwipeRefreshLayout refresh = null;

    final private int numNeeded = 5; //max number of items displayed at once
    private int numSkipped = 0; //holds number of items we have displayed so far on screen
    private int numIn = 0; //holds number of items in parse as of page startup

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        checkUser();

        numIn = parse.countThemes();

        CustomListView = this;
        refresh = ( SwipeRefreshLayout )findViewById( R.id.refresh );
        refresh.setRefreshing(true);
        setListData.run();

        Resources res = getResources();
        list = ( ListView )findViewById( R.id.list );


        //make footer with inflator for when we are loading more themes
        footer = ( (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer, null, false);
        //make footer with inflator for when we have no more items to display
        noItemFooter = ( (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.noitemfooter, null, false);

        list.addFooterView(footer); //ad the footer to bottom of list

        adapter = new CustomAdapter( CustomListView, CustomListViewValuesArr,res);
        list.setAdapter( adapter );


        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;

                if (numSkipped != numIn && numSkipped < numIn) {
                    if ((lastInScreen == totalItemCount) && !(loadingMore)) {
                        Thread thread = new Thread(null, loadMoreListItems);
                        thread.start();
                    }
                } else if (numSkipped == numIn) {
                    list.removeFooterView(footer);
                    list.addFooterView(noItemFooter);
                    noMore = true;
                    numSkipped++;
                }
            }
        });

        refresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.d("REFRESH", "onRefresh called from SwipeRefreshLayout");
                        doRefresh();
                    }
                }
        );
    }

    public Runnable setListData = new Runnable()
    {
        @Override
                public void run() {
            ArrayList<ArrayList<String>> themes = parse.getThemes(numNeeded, numSkipped);


            for (int i = 0; i < themes.size(); i++) {
                ArrayList<String> current = themes.get(i);
                final ListModel sched = new ListModel();

                String tempAuth = current.get(4);
                if (tempAuth.isEmpty()) {tempAuth="Powered by Wordnik";}

                sched.setLobbyTitle(current.get(3));
                sched.setLobbyAuthor("By: " + tempAuth);
                sched.setExpireDate(current.get(1));
                sched.setLobbyDes(current.get(2));
                sched.setLobbyID(current.get(0));
                sched.setTopPun(current.get(5));

                CustomListViewValuesArr.add(sched);
            }

            numSkipped += themes.size();
            if (refresh.isRefreshing()) {
                refresh.setRefreshing(false);
            }
        }
    };

    public void onItemClick(int mPosition){
        ListModel tempValues = ( ListModel ) CustomListViewValuesArr.get(mPosition);

        Intent i = new Intent(Lobby.this, Puns.class);
        i.putExtra("LOBBY_ID", tempValues.getLobbyID());
        i.putExtra("THEME_TITLE", tempValues.getLobbyTitle());
        i.putExtra("THEME_DESC", tempValues.getLobbyDes());
        i.putExtra("THEME_AUTHOR", tempValues.getLobbyAuthor());
        i.putExtra("THEME_EXPIRE", tempValues.getExpireDate());
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lobby, menu);
        return true;
    }

    //Never ending listView runnable #1 - initiation
    private Runnable loadMoreListItems = new Runnable() {
        @Override
        public void run() {
            loadingMore = true;


            try {
                Thread.sleep(750);
            } catch (InterruptedException e) {
            }

            runOnUiThread(returnRes);
        }
    };

    //Never ending listView runnable #2 - filling and updating listView
    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
            setListData.run();

            adapter.notifyDataSetChanged();

            loadingMore = false;
        }
    };

    public void checkUser(){
    ParseApplication parse = new ParseApplication();
    if(!parse.doesUserExist(com.facebook.Profile.getCurrentProfile().getId()))
    {
        com.facebook.Profile profile = com.facebook.Profile.getCurrentProfile();
        if(profile != null) {
            parse.createNewUser(com.facebook.Profile.getCurrentProfile().getId(), profile.getFirstName());
        }
    }
}

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

    public void doRefresh()
    {
        CustomListViewValuesArr.clear();
        if (noMore) {
            list.removeFooterView(noItemFooter);
            list.addFooterView(footer);
            noMore = false;
        }
        numSkipped = 0;
        numIn = parse.countThemes();
        adapter.notifyDataSetChanged();
        //refresh.setRefreshing(false);
    }
}
