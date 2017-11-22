package watson.punwarz;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import watson.punwarz.ImageView.RoundedImageView;
import watson.punwarz.ListView.CustomThemeAdapter;
import watson.punwarz.ListView.ListModel;
import watson.punwarz.ListView.PunModel;
import watson.punwarz.ListView.UserPunAdapter;
import watson.punwarz.ListView.UserTopPunAdapter;

import com.facebook.FacebookSdk;

import java.util.ArrayList;


/**
 * @author Daniel Tetzlaff (tetzlaffdanielj@gmail.com)
 * @version 1.2
 * Created: 2016-03-08
 * Updated: 2017-11-22
 * Description: Provides summary of user based on the userID provided via Intent, utilizes inheritance from Page.class
 */
public class Profile extends Page
{
    //initialization of variable names for holding items that we will update
    private TextView name;
    private TextView points;
    private TextView rankText;
    private RoundedImageView profilePic;
    private Bitmap tempPic;
    private com.facebook.Profile profile;
    private ParseApplication parse;
    private int userPoints;
    private String userID;
    private Boolean isFriend = false;

    //initialization of listviews and corresponding adapters
    ListView themeList;
    ListView punList;
    ListView topPunList;
    CustomThemeAdapter themeAdapter;
    UserPunAdapter punAdapter;
    UserTopPunAdapter topPunAdapter;


    //array lists initialized for use with never-ending listviews
    public Profile CustomListView = null;
    public ArrayList<ListModel> CustomListViewValuesArrTheme = new ArrayList<ListModel>();
    public ArrayList<PunModel> CustomListViewValuesArrPun = new ArrayList<PunModel>();
    public ArrayList<PunModel> CustomListViewValuesArrTopPun = new ArrayList<PunModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar); //set toolbar

        //Set textViews here
        name = (TextView)findViewById(R.id.name);
        points = (TextView)findViewById(R.id.points);
        rankText = (TextView)findViewById(R.id.rank);
        profilePic = (RoundedImageView)findViewById(R.id.profilePic);

        setSupportActionBar(toolbar);

        //get the UserID that was passed through intent
        userID = this.getIntent().getStringExtra("UserID");

        if (userID == null) //if this was null, nothing passed through intent ∴ user is viewing their own profile
        {
            //get current user's ID
            profile = com.facebook.Profile.getCurrentProfile();
            userID = profile.getId();
            isFriend = true;
        }

        //initialize DB
        parse = new ParseApplication();

        CustomListView = this;

        setName();
        setPoints();
        setRank();

        //AsyncTaskss that make DB calls that may be longer and could cause delays if not async
        new setProfilePic().execute(userID);
        new SetThemeList().execute(userID);
        new SetPunList().execute(userID);
        new SetTopPunList().execute(userID);

        Resources res = getResources();
        themeList = ( ListView )findViewById( R.id.user_themes_list );
        punList = ( ListView )findViewById( R.id.user_puns_list );
        topPunList = ( ListView )findViewById(R.id.user_topPuns_list);

        themeAdapter = new CustomThemeAdapter( CustomListView, CustomListViewValuesArrTheme, res);
        themeList.setAdapter( themeAdapter );

        punAdapter = new UserPunAdapter( CustomListView, CustomListViewValuesArrPun, res);
        punList.setAdapter(punAdapter);

        topPunAdapter = new UserTopPunAdapter( CustomListView, CustomListViewValuesArrTopPun, res);
        topPunList.setAdapter(topPunAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        if (isFriend)
        {
            menu.findItem(R.id.addfriend_settings).setVisible(false);
        }
        return true;
    }

    private void setName(){
        name.setText(parse.getUserName(userID));
    }

    private void setPoints()
    {
        userPoints = parse.getUserPoints(userID);
        points.setText("Points: " + Integer.toString(userPoints));
    }

    private void setRank()
    {
        String rank;

        if (userPoints < 30){ rank = "Punching Bag"; }
        else if (userPoints < 60){ rank = "Pun'kd"; }
        else if (userPoints < 100){ rank = "Depundable"; }
        else if (userPoints < 140){ rank = "Pungent"; }
        else if (userPoints < 190){ rank = "Puntagon"; }
        else if (userPoints < 240){ rank = "Punny"; }
        else if (userPoints < 300){ rank = "Punctual"; }
        else if (userPoints < 360){ rank = "Puntastic"; }
        else if (userPoints < 430){ rank = "Punchier"; }
        else if (userPoints < 500){ rank = "Punderful"; }
        else if (userPoints < 580){ rank = "Cyberpun"; }
        else { rank = "Punisher"; }

        rankText.setText("Rank: " + rank);
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
        protected void onPostExecute(Long result) {
            profilePic.setImageBitmap(tempPic);
        }
    }

    private class SetThemeList extends AsyncTask<String, Integer, Long>
    {
        @Override
        protected Long doInBackground(String... userID)
        {
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) { }
//            Sleep to test loading spinner

            ArrayList<ArrayList<String>> themes = parse.getUserThemes(userID[0]);

            for (int i = 0; i < themes.size(); i++)
            {
                ArrayList<String> current = themes.get(i);
                final ListModel sched = new ListModel();

                sched.setLobbyTitle(current.get(0));
                sched.setExpireDate(current.get(1));
                sched.setLobbyDes(current.get(2));
                sched.setTopPun(current.get(3));
                sched.setLobbyAuthor(current.get(4));
                sched.setLobbyID(current.get(5));

                CustomListViewValuesArrTheme.add(sched);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long result)
        {
            findViewById(R.id.themeProgressBar).setVisibility(View.INVISIBLE);
            themeAdapter.notifyDataSetChanged();
        }
    }

    private class SetPunList extends AsyncTask<String, Integer, Long>
    {
        @Override
        protected Long doInBackground(String... userID)
        {
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//            Sleep to test loading spinner

            ArrayList<ArrayList<String>> puns = parse.getUserPuns(userID[0]);

            for (int i = 0; i < puns.size(); i++)
            {
                ArrayList<String> current = puns.get(i);
                final PunModel sched = new PunModel();

                sched.setPun(current.get(0));
                sched.setPunVotes(current.get(1));
                sched.setThemeTitle(current.get(2));
                sched.setThemeID(current.get(3));
                sched.setThemeAuth(current.get(4));
                sched.setThemeDesc(current.get(5));
                sched.setThemeExp(current.get(6));

                CustomListViewValuesArrPun.add(sched);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Long result)
        {
            findViewById(R.id.punProgressBar).setVisibility(View.INVISIBLE);
            punAdapter.notifyDataSetChanged();
        }
    }

    private class SetTopPunList extends AsyncTask<String, Integer, Long>
    {
        @Override
        protected Long doInBackground(String... userID)
        {
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//            Sleep to test loading spinner

            ArrayList<ArrayList<String>> puns = parse.getUserTopPuns(userID[0]);

            for (int i = 0; i < puns.size(); i++)
            {
                ArrayList<String> current = puns.get(i);
                final PunModel sched = new PunModel();

                sched.setPun(current.get(0));
                sched.setPunVotes(current.get(1));
                sched.setThemeTitle(current.get(2));

                CustomListViewValuesArrTopPun.add(sched);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long result)
        {
            findViewById(R.id.topPunProgressBar).setVisibility(View.INVISIBLE);
            topPunAdapter.notifyDataSetChanged();
        }
    }

    public void onThemeItemClick(int mPosition)
    {
        ListModel tempValues = ( ListModel ) CustomListViewValuesArrTheme.get(mPosition);

        Intent i = new Intent(Profile.this, Puns.class);
        i.putExtra("LOBBY_ID", tempValues.getLobbyID());
        i.putExtra("THEME_TITLE", tempValues.getLobbyTitle());
        i.putExtra("THEME_DESC", tempValues.getLobbyDes());
        i.putExtra("THEME_AUTHOR", tempValues.getLobbyAuthor());
        i.putExtra("THEME_EXPIRE", tempValues.getExpireDate());
        startActivity(i);
    }

    public void onPunItemClick(int mPosition)
    {
        PunModel tempValues = ( PunModel ) CustomListViewValuesArrPun.get(mPosition);

        Intent i = new Intent(Profile.this, Puns.class);
        i.putExtra("LOBBY_ID", tempValues.getThemeID());
        i.putExtra("THEME_TITLE", tempValues.getThemeTitle());
        i.putExtra("THEME_DESC", tempValues.getThemeDesc());
        i.putExtra("THEME_AUTHOR", tempValues.getThemeAuth());
        i.putExtra("THEME_EXPIRE", tempValues.getThemeExp());
        startActivity(i);
    }
}
