package watson.punwarz;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import android.graphics.Bitmap;
import java.util.ArrayList;

import watson.punwarz.ListView.CustomLeaderAdapter;
import watson.punwarz.ListView.LeaderModel;

//TODO DOCUMENTATION
/**
 * @author Daniel Tetzlaff
 * @version 1.0
 * Created: 2017-11-07
 *
 * Description: This class will display and control the Leaderboard (roughly an extension based on the lobby page)
 */
public class Leaderboard extends Page
{
    ListView list;
    View noItemFooter;  // holder for "no more items" footer
    View footer;        // holder for "loading more items" footer
    CustomLeaderAdapter adapter;
    boolean noMore = false;
    Bitmap tempPic;

    public Leaderboard CustomListView = null;
    public  ArrayList<LeaderModel> CustomListViewValuesArr = new ArrayList<LeaderModel>();
    private boolean loadingMore = false;
    private ParseApplication parse = new ParseApplication();
    public SwipeRefreshLayout refresh = null;

    private int curLeadNum = 0;
    final private int numNeeded = 5; //max number of items displayed at once
    private int numSkipped = 0; //holds number of items we have displayed so far on screen
    private int numIn = 0; //holds number of items in parse as of page startup

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

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

        adapter = new CustomLeaderAdapter( CustomListView, CustomListViewValuesArr,res);
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
            LeaderModel leader = (LeaderModel) CustomListViewValuesArr.get(curLeadNum);
            leader.setLeaderImg(tempPic);
            adapter.notifyDataSetChanged();
            curLeadNum++;
        }
    }

    public Runnable setListData = new Runnable()
    {
        @Override
                public void run() {
            ArrayList<ArrayList<String>> users = parse.getUserLeaderboard(numNeeded, numSkipped);

            for (int i = 0; i < users.size(); i++) {
                ArrayList<String> current = users.get(i);
                final LeaderModel sched = new LeaderModel();

                sched.setLeaderName(current.get(0));
                sched.setPos(current.get(3));
                sched.setLeaderScore(current.get(2));
                sched.setLeaderID(current.get(4));

                new setProfilePic().execute(current.get(4));
                CustomListViewValuesArr.add(sched);
            }

            numSkipped += users.size();
            if (refresh.isRefreshing()) {
                refresh.setRefreshing(false);
            }
        }
    };

    public void onLeaderItemClick(int mPosition){
        LeaderModel tempValues = ( LeaderModel ) CustomListViewValuesArr.get(mPosition);
        Intent i = new Intent(Leaderboard.this, Profile.class);
        i.putExtra("UserID", tempValues.getLeaderID());
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_leaderboard, menu);
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
        numIn = parse.countUsers();
        adapter.notifyDataSetChanged();
        curLeadNum = 0;
        //refresh.setRefreshing(false);
    }
}
