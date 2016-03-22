package watson.punwarz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import watson.punwarz.ImageView.RoundedImageView;
import com.facebook.FacebookSdk;

import java.io.IOException;
import java.net.URL;


/**
 * Author: Carille
 * Created: 2016-03-08
 * Desc: Using inheritance, takes after page and will display user, the user's points, rank, themes created, and puns submitted.
 */
public class Profile extends Page
{
    private TextView name;
    private TextView points;
    private TextView rankText;
    private RoundedImageView profilePic;
    private com.facebook.Profile profile;
    private ParseApplication parse;
    private int userPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);

        //Set textViews here
        name = (TextView)findViewById(R.id.name);
        points = (TextView)findViewById(R.id.points);
        rankText = (TextView)findViewById(R.id.rank);
        profilePic = (RoundedImageView)findViewById(R.id.profilePic);


        setSupportActionBar(toolbar);
        profile = com.facebook.Profile.getCurrentProfile();
        parse = new ParseApplication();

        setName();
        setPoints();
        setRank();
        setProfilePic();
    }

    private void setName(){
        name.setText(profile.getName());
    }

    private void setPoints(){
        userPoints = parse.getUserPoints(profile.getId());
        points.setText("Points: " + Integer.toString(userPoints));
    }

    private void setRank(){
        String rank;

        if (userPoints < 10){
            rank = "Punching Bag";
        }
        else if (userPoints < 30){
            rank = "Depundable";
        }
        else if (userPoints < 75){
            rank = "Punderful";
        }
        else {
            rank = "Punisher";
        }

        rankText.setText("Rank: " + rank);
    }

    private void setProfilePic(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri uri = profile.getProfilePictureUri(150, 150);

                try{
                    URL facebookProfileURL = new URL(uri.toString());
                    final Bitmap bitmap = BitmapFactory.decodeStream(facebookProfileURL.openConnection().getInputStream());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            profilePic.setImageBitmap(bitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
