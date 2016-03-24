package watson.punwarz;

import android.app.Application;
import android.util.Log;

import com.parse.*;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ParseApplication extends Application {

    private String userObjectID = null; //the active user
    private String tempLobbyObjectID = null; //since Parse queries can't actually return???
    private String tempPostObjectID = null;
    private boolean exists = false; //all-purpose boolean for query results.
    private ParseObject activeObject = null;
    private SimpleDateFormat format = new SimpleDateFormat("MMM d, ''yy");

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, getResources().getString(R.string.parse_app_id), getResources().getString(R.string.parse_client_id));
    }

    //creates a new user object in Parse if the user has not previously used app.
    public void createNewUser(String facebookID, String displayName) {
        ParseObject newUser = new ParseObject("Users");
        newUser.put("UserID", facebookID);
        newUser.put("DisplayName", displayName);
        newUser.put("Score", 0);
        userObjectID = newUser.getObjectId(); //This may have to be after the save??
        newUser.saveInBackground();
    }

    //creates a new lobby associated with the active user
    public void createNewLobby(String userID, String lobbyTheme, String lobbyDesc, Date expiryDate){
        ParseObject newLobby = new ParseObject("Lobby");
        newLobby.put("Theme", lobbyTheme);
        newLobby.put("Desc", lobbyDesc);
        newLobby.put("UserID", userID);
        newLobby.put("ExpiryDate", expiryDate);
        newLobby.saveInBackground();
    }

    //creates a new Post associated to a particular Lobby
    public void createNewPun(String userID, String lobbyID, String punText){
        ParseObject newPost = new ParseObject("Posts");
        newPost.put("UserID", userID);
        newPost.put("LobbyID", lobbyID);
        newPost.put("Pun", punText);
        newPost.put("Score", 0);
        newPost.saveInBackground();
        //String postObjectID = newPost.getObjectId(); //may have to move this before the save??
    }

    //sets the userObjectID variable so that it does not have to be queried multiple times.
    private void setUserObjectID(final String FacebookID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("UserID", FacebookID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    //The getFirst request failed.
                } else {
                    //Retrieved the object.
                    userObjectID = object.getObjectId();
                }
            }
        });

    }

    //Checks if an object from the selected tableName, in the selected tableRow, and the searchedValue already exists
    //This is the general use function.
    private boolean checkIfObjectExists(String tableName, String tableRow, String searchedValue) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(tableName);
        query.whereEqualTo(tableRow, searchedValue);
        boolean objectExists = false;
        try {
            ParseObject object = query.getFirst();
            if (object == null) {
                //Object does not exist.
                objectExists = false;
            } else {
                //Object does exist already.
                objectExists = true;
            }
        } catch (ParseException e) {
        }
        return objectExists;
    }




    //runs a query and returns a ParseObject for manipulation.
    private ParseObject getParseObject(String tableName, String objectID){

        ParseQuery<ParseObject> query = ParseQuery.getQuery(tableName);
        query.whereEqualTo("ObjectID", objectID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    //Object does not exist.

                } else {
                    //Object does exist.
                    activeObject = object;
                }
            }
        });
        return activeObject;
    }


    //Checks if a given Facebook user ID is already in the Parse database
    public boolean doesUserExist(String facebookID) {
        exists = false;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("UserID", facebookID);
        try {
            query.getFirst();
            exists = true;
        } catch (ParseException e) {
            exists = false;
        }


        //Log.d("EXISTS#####", Boolean.toString(exists)); debugging log
        return exists;
    }

    public boolean doesThemeExist(String prompt)
    {
        boolean exist = false;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Lobby");
        query.whereEqualTo("Theme", prompt);
        try {
            query.getFirst();
            exist = true;
        } catch (ParseException p){
            exist = false;
        }
        return exist;
    }

    public int getUserPoints(String facebookID){
        int result = 0;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("UserID", facebookID);
        try {
            result = query.getFirst().getInt("Score");
        } catch (ParseException e) {
            Log.d("PARSE ERROR", "-Error retrieving points-");
        }

        return result;
    }

    public ArrayList<ArrayList<String>> getThemes(int numNeeded, int numSkipped) {
        ArrayList<ArrayList<String>> themes = new  ArrayList<ArrayList<String>>();
        ArrayList<String> singleTheme = new ArrayList<String>();
        List<ParseObject> list;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Lobby");
        query.setLimit(numNeeded);
        query.setSkip(numSkipped);

        Calendar cal = Calendar.getInstance();

        query.whereGreaterThanOrEqualTo("ExpiryDate", cal.getTime()).getLimit();
        query.orderByAscending("ExpiryDate");
        try {
            list = query.find();

            for (int i = 0 ; i < list.size() ; i++){
                singleTheme = new ArrayList<String>();
                ParseObject cur = list.get(i);
                singleTheme.add(cur.getObjectId());
                singleTheme.add(format.format(cur.getDate("ExpiryDate")).toString());
                singleTheme.add(cur.getString("Desc"));
                singleTheme.add(cur.getString("Theme"));
                singleTheme.add(getUserName(cur.getString("UserID")));
                singleTheme.add(getTopPun(cur.getObjectId()));

                themes.add(singleTheme);
            }
        }
        catch (ParseException e) {
            Log.d("PARSE ERROR", "-Error retrieving Theme-");
        }

    return themes;
    }

    public String getTopPun (String themeID){
        String result = "Lobby is Empty";

        ParseQuery<ParseObject> themes = ParseQuery.getQuery("Posts");
        themes.whereEqualTo("LobbyID", themeID);
        themes.addDescendingOrder("Score");
        try {
           ParseObject top = themes.getFirst();
            result = "Top Pun: " + top.getString("Pun");
        }
        catch (ParseException e){}

        return result;
    }

    public int countThemes(){
        int result = 0;
        Calendar cal = Calendar.getInstance();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Lobby");

        try {
            query.whereGreaterThanOrEqualTo("ExpiryDate", cal.getTime());
            result = query.count();
        } catch (ParseException e){
            Log.d("PARSE ERROR", "-Error retrieving Theme-");
        }

        return result;
    }

    public String getUserName(String facebookID){
        String result = "";

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("UserID", facebookID);

        try{
            result = query.getFirst().getString("DisplayName");
        } catch (ParseException e) {
            Log.d("PARSE ERROR", "-Error retrieving name-");
        }

        return result;
    }

    public ArrayList<ArrayList<String>> getPuns(String lobbyID){
        ArrayList<ArrayList<String>> puns = new ArrayList<ArrayList<String>>();
        ArrayList<String> singlePun;
        List<ParseObject> list;


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.whereEqualTo("LobbyID", lobbyID);

        try {
                list = query.find();

                for (int i=0; i < list.size(); i++) {
                    singlePun = new ArrayList<String>();
                    ParseObject cur = list.get(i);
                    singlePun.add(cur.getString("Pun"));
                    singlePun.add(getUserName(cur.getString("UserID")));
                    singlePun.add(cur.getObjectId());
                    singlePun.add(cur.getString("UserID"));
                    puns.add(singlePun);
                }
        } catch (ParseException e) {
            Log.d("PARSE ERROR", "-Error retrieving puns-");
        }

        return puns;
    }

    //checks if a lobby with an existing prompt description already exists. currently not case sensitive
    public boolean doesLobbyExist(String description){
        clearTempVars();
        checkIfObjectExists("Lobby", "Prompt", description);
        return exists;
    }

    public String getLobbyObjectID(){
        return tempLobbyObjectID;
    }

    //resets the Global variables to ensure data from previous calls does not persist
    private void clearTempVars(){
        tempLobbyObjectID = null;
        tempPostObjectID = null;
        exists = false;
    }

    //Call this when logging out to clear the userObjectID
    private void deactivateUser(){
        userObjectID = null;
        activeObject = null;
        clearTempVars();
    }

    //Checks to see if a given user has voted on a given post already and returns a boolean.
    private boolean canVoteOn(String voterID, String postID){
        boolean canVote = false;
        String postOwnerID;

            ParseQuery<ParseObject> postOwner = ParseQuery.getQuery("Posts");
            try {
            ParseObject lobby = postOwner.get(postID);
                postOwnerID = lobby.getString("UserID");
             }
            catch (ParseException p){ postOwnerID = "";}

            if(postOwnerID.equals(voterID)) //voter is post creator, do not allow vote
            {canVote = false;}
            else {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Votes");
                query.whereEqualTo("VoterID", voterID);
                query.whereEqualTo("PostID", postID);
                try {
                    query.getFirst();
                    canVote = false;
                } catch (ParseException p) {
                    canVote = true;
                }
            }

        return canVote;
    }

    //User votes for a post, incrementing the post score, creators score, and creates vote record
    public void voteOnPost(String voterID, String postID, String lobbyID) {

        if (canVoteOn(voterID, postID)) {
            //Increment Post Score
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
            query.getInBackground(postID, new GetCallback<ParseObject>() {
                public void done(ParseObject post, ParseException e) {
                    if (e == null) {
                        post.increment("Score");
                        post.saveInBackground();

                        String creatorID = post.getString("UserID");

                        //Increment score of posts creator
                        //Start of nested query
                        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Users");
                        query2.getInBackground(creatorID, new GetCallback<ParseObject>() {
                            public void done(ParseObject user, ParseException e) {
                                if (e == null) {
                                    user.increment("Score");
                                    user.saveInBackground();
                                } else {
                                    //should never hit here
                                }
                            }
                        });
                        //end of nested query
                    } else {
                        //should never hit here
                    }
                }
            });

            //creates record of vote
            ParseObject newVote = new ParseObject("Votes");
            newVote.put("VoterID", voterID);
            newVote.put("PostID", postID);
            newVote.put("LobbyID", lobbyID);
            newVote.saveInBackground();

        }
        //nothing happens if the IF statement wasn't true
    }


}
