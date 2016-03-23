package watson.punwarz;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.MenuInflater;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import watson.punwarz.ListView.PunAdapter;
import watson.punwarz.ListView.PunModel;
import com.facebook.FacebookSdk;

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
    private String lobbyID;
    private ParseApplication parse;
    private View header;

    private String title = "";
    private String desc = "";
    private String author = "";
    private String expDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puns);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        header = ( (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ).inflate(R.layout.themeheader, null, false);

        parse = new ParseApplication();

        Intent intent = getIntent();
        lobbyID = intent.getStringExtra("LOBBY_ID");
        getExtras(intent);

        CustomListView = this;

        setListData();

        Resources res = getResources();
        list = ( ListView )findViewById( R.id.list );

        adapter = new PunAdapter( CustomListView, CustomListViewValuesArr,res);
        list.setAdapter( adapter );

        //list.addHeaderView(header);
        setHeader();
    }

    public void onItemClick(int position)
    {
        PunModel tempValues = (PunModel)CustomListViewValuesArr.get(position);

        //lobbyID
        //tempValues.ge
    }

    public void setListData()
    {
        ArrayList<ArrayList<String>> puns = parse.getPuns(lobbyID);


        for (int i = 0; i < puns.size(); i++){
            ArrayList<String> current = puns.get(i);
            final PunModel sched = new PunModel();

                sched.setPunAuth("By: " + current.get(1));
                sched.setPun(current.get(0));
                sched.setPunVotes("num " + i + lobbyID);

            CustomListViewValuesArr.add(sched);
        }
    }

    public void setHeader(){
        TextView titleView = (TextView)findViewById(R.id.lobbyTitle);
        TextView descView = (TextView)findViewById(R.id.lobbyDes);
        TextView authView = (TextView)findViewById(R.id.lobbyAuthor);
        TextView expView = (TextView)findViewById(R.id.expireDate);

        titleView.setText(title);
        descView.setText(desc);
        authView.setText(author);
        expView.setText(expDate);
    }

    private void getExtras(Intent intent){
        title = intent.getStringExtra("THEME_TITLE");
        desc = intent.getStringExtra("THEME_DESC");
        expDate = intent.getStringExtra("THEME_EXPIRE");
        author = intent.getStringExtra("THEME_AUTHOR");
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
        i.putExtra("THEME_TITLE", title);
        startActivity(i);
    }
}
