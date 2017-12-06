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

/**
 * @author Daniel Tetzlaff
 * @version 1.0
 * Created: 2017-12-05
 *
 * Description: This class will display and control the Friend Requests for a user (roughly an extension based on the lobby page)
 */
public class FriendRequests extends Page
{
    ListView list;                  //list containing all friend request items
    CustomRequestAdapter adapter;
    Bitmap tempPic;                 //temporary holder for profile images
    String userID;                  //holder for current userID
    ArrayList<String> picBeingSet = new ArrayList<String>();

    public FriendRequests CustomListView = null;
    public  ArrayList<RequestModel> CustomListViewValuesArr = new ArrayList<RequestModel>();
    private ParseApplication parse = new ParseApplication();
    public SwipeRefreshLayout refresh = null;

    private int curReqNum = 0; //counter to keep track of loading images

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
                        if (picBeingSet.size() == 0)
                        {
                            Log.d("REFRESH-REQUESTS-SS", "onRefresh called from SwipeRefreshLayout on Friend Requests");
                            doRefresh();
                        }
                        else
                        {
                            Log.d("REFRESH-REQUESTS-XX", "Friend Requests cannot refresh, loading pictures");
                            refresh.setRefreshing(false);
                        }
                    }
                }
        );
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
            RequestModel request = (RequestModel) CustomListViewValuesArr.get(curReqNum);
            request.setRequestImg(tempPic);
            adapter.notifyDataSetChanged();
            curReqNum++;
            picBeingSet.remove(0);
        }
    }

    public Runnable setListData = new Runnable()
    {
        @Override
        public void run() {
            ArrayList<ArrayList<String>> users = parse.getFriendRequests(userID);

            for (int i = 0; i < users.size(); i++) {
                ArrayList<String> current = users.get(i);
                final RequestModel sched = new RequestModel();

                sched.setRequestID(current.get(0));
                sched.setRequestName(current.get(1));

                new FriendRequests.setProfilePic().execute(current.get(0));
                picBeingSet.add(current.get(0));
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
