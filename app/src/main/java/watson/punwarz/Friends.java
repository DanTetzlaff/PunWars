package watson.punwarz;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

import watson.punwarz.ListView.CustomFriendAdapter;
import watson.punwarz.ListView.FriendModel;

/**
 * @author Daniel Tetzlaff
 * @version 1.0
 * Created: 2017-12-01
 *
 * Description: Class will display and control the Friends page (similar to the lobby and leaderboard classes, following same structure)
 */

public class Friends extends Page
{
    ListView list; // holder for main list on page
    View noItemFooter; // holder for "no more items" footer
    View footer; // holder for "loading more items" footer
    CustomFriendAdapter adapter;
    boolean noMore = false;
    Bitmap tempPic;

    public Friends CustomListView = null;
    public ArrayList<FriendModel> CustomListViewValuesArr = new ArrayList<FriendModel>();
    private boolean loadingMore = false;
    private ParseApplication parse = new ParseApplication();
    public SwipeRefreshLayout refresh = null;

    private int curFriendNum = 0;
    final private int numNeeded = 5; //max number of items displayed at once
    private int numSkipped = 0; //holds number of items we have displayed so far on screen
    private int numIn = 0; //holds number of items in parse as of page startup

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        //TODO count number of friends current user has
        numIn = parse.countUsers();

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

        adapter = new CustomFriendAdapter( CustomListView, CustomListViewValuesArr,res);
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

    private static class setProfilePicParams {
        String userID;
        int mPosition;

        setProfilePicParams(String userID, int mPosition) {
            this.userID = userID;
            this.mPosition = mPosition;
        }
    }

    private static class imageParams {
        Bitmap img;
        int mPosition;

        imageParams(Bitmap img, int mPosition) {
            this.img = img;
            this.mPosition = mPosition;
        }
    }

    private class setProfilePic extends AsyncTask<String, Integer, Long>
    {
        @Override
        protected Long doInBackground(String... userID)
        {
            PictureGrabber pic = new PictureGrabber();
            tempPic = pic.getUserPicture(getApplicationContext(), userID[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Long result)
        {
            FriendModel friend = (FriendModel) CustomListViewValuesArr.get(curFriendNum);
            friend.setFriendImg(tempPic);
            adapter.notifyDataSetChanged();
            curFriendNum++;
        }
    }

    public Runnable setListData = new Runnable()
    {
        @Override
        public void run() {
            //TODO chnage to get current user's friends
            ArrayList<ArrayList<String>> users = parse.getUserLeaderboard(numNeeded, numSkipped);
            Ranks rank = new Ranks();

            for (int i = 0; i < users.size(); i++) {
                ArrayList<String> current = users.get(i);
                final FriendModel sched = new FriendModel();

                sched.setFriendName(current.get(0));
                sched.setFriendScore(current.get(1));
                sched.setFreindID(current.get(2));

                new Friends.setProfilePic().execute(current.get(3));
                CustomListViewValuesArr.add(sched);
            }

            numSkipped += users.size();
            if (refresh.isRefreshing()) {
                refresh.setRefreshing(false);
            }
        }
    };

    public void onFriendItemClick(int mPosition){
        FriendModel tempValues = ( FriendModel ) CustomListViewValuesArr.get(mPosition);
        Intent i = new Intent(Friends.this, Profile.class);
        i.putExtra("UserID", tempValues.getFriendID());
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_friends, menu);
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


    public void doRefresh()
    {
        CustomListViewValuesArr.clear();
        if (noMore) {
            list.removeFooterView(noItemFooter);
            list.addFooterView(footer);
            noMore = false;
        }
        numSkipped = 0;
        //TODO change to count friends for current user
        numIn = parse.countUsers();
        adapter.notifyDataSetChanged();
        curFriendNum = 0;
        //refresh.setRefreshing(false);
    }
}
