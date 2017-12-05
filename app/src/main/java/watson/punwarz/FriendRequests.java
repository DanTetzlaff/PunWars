package watson.punwarz;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

import com.facebook.FacebookSdk;

import java.util.ArrayList;

import watson.punwarz.ListView.CustomRequestAdapter;
import watson.punwarz.ListView.RequestModel;

//TODO DOCUMENTATION

/**
 * @author Daniel Tetzlaff
 * @version 1.0
 * Created: 2017-12-05
 *
 * Description: This class will display and control the Leaderboard (roughly an extension based on the lobby page)
 */
public class FriendRequests extends Page
{
    ListView list;
    CustomRequestAdapter adapter;
    Bitmap tempPic;
    String userID;

    public FriendRequests CustomListView = null;
    public  ArrayList<RequestModel> CustomListViewValuesArr = new ArrayList<RequestModel>();
    private ParseApplication parse = new ParseApplication();
    public SwipeRefreshLayout refresh = null;

    private int curReqNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendrequests);
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        FacebookSdk.sdkInitialize(getApplicationContext());


        userID = com.facebook.Profile.getCurrentProfile().getId();

        CustomListView = this;
        refresh = ( SwipeRefreshLayout )findViewById( R.id.refreshRequests );
        refresh.setRefreshing(true);
        setListData.run();

        Resources res = getResources();
        list = ( ListView )findViewById( R.id.list );

        adapter = new CustomRequestAdapter( CustomListView, CustomListViewValuesArr,res);
        list.setAdapter( adapter );

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
            RequestModel leader = (RequestModel) CustomListViewValuesArr.get(curReqNum);
            leader.setRequestImg(tempPic);
            adapter.notifyDataSetChanged();
            curReqNum++;
        }
    }

    public Runnable setListData = new Runnable()
    {
        @Override
                public void run() {
            ArrayList<ArrayList<String>> users = parse.getFriendRequests(userID);
            Ranks rank = new Ranks();

            for (int i = 0; i < users.size(); i++) {
                ArrayList<String> current = users.get(i);
                final RequestModel sched = new RequestModel();

                sched.setRequestID(current.get(0));
                sched.setRequestName(current.get(1));

                new setProfilePic().execute(current.get(2));
                CustomListViewValuesArr.add(sched);
            }

            if (refresh.isRefreshing()) {
                refresh.setRefreshing(false);
            }
        }
    };

    public void onRequestItemClick(int mPosition){
        RequestModel tempValues = ( RequestModel ) CustomListViewValuesArr.get(mPosition);
        Intent i = new Intent(FriendRequests.this, Profile.class);
        i.putExtra("UserID", tempValues.getRequestID());
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_friendrequests, menu);
        return true;
    }


    public void doRefresh()
    {
        CustomListViewValuesArr.clear();

        adapter.notifyDataSetChanged();
        curReqNum = 0;
        //refresh.setRefreshing(false);
    }
}
