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

    public boolean isLobbyExpired(String lobbyID) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Lobby");
        Calendar cal = Calendar.getInstance();
        List<ParseObject> object;
        boolean result = true;

        query.whereEqualTo("objectId", lobbyID);
        query.whereGreaterThanOrEqualTo("ExpiryDate", cal.getTime());
        try {
            object = query.find();

            if (object.size() != 0) { result = false; }

        } catch (ParseException e) {
            Log.d("PARSE ERROR", "-Error checking Theme-");
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

    public ArrayList<ArrayList<String>> getUserThemes(String userID) {
        ArrayList<ArrayList<String>> themes = new ArrayList<ArrayList<String>>();
        ArrayList<String> singleTheme = new ArrayList<String>();
        List<ParseObject> list;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Lobby");
        Calendar cal = Calendar.getInstance();

        query.whereEqualTo("UserID", userID);
        query.whereGreaterThanOrEqualTo("ExpiryDate", cal.getTime());
        query.orderByAscending("ExpiryDate");
        try {
            list = query.find();

            for (int i = 0; i < list.size(); i++) {
                singleTheme = new ArrayList<String>();
                ParseObject cur = list.get(i);

                singleTheme.add(cur.getString("Theme"));
                singleTheme.add(format.format(cur.getDate("ExpiryDate")).toString());
                singleTheme.add(cur.getString("Desc"));
                singleTheme.add(getUserName(cur.getString("UserID")));
                singleTheme.add(getUserName(cur.getString("UserID")));
                singleTheme.add(cur.getObjectId());

                themes.add(singleTheme);
            }

        } catch (ParseException e) {
            Log.d("PARSE ERROR", "-Error retrieving userTheme-");
        }

        return themes;
    }

    public String getTopPun (String themeID){
        String result = "Lobby Has No Top Pun Yet";

        ParseQuery<ParseObject> themes = ParseQuery.getQuery("Posts");
        themes.whereEqualTo("LobbyID", themeID);
        themes.addDescendingOrder("Score");
        themes.addAscendingOrder("createdAt");
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
        query.addDescendingOrder("Score");
        query.addAscendingOrder("createdAt");
        try {
                list = query.find();

                for (int i=0; i < list.size(); i++) {
                    singlePun = new ArrayList<String>();
                    ParseObject cur = list.get(i);
                    singlePun.add(cur.getString("Pun"));
                    singlePun.add(getUserName(cur.getString("UserID")));
                    singlePun.add(cur.getObjectId());
                    singlePun.add(cur.getString("UserID"));
                    singlePun.add(Integer.toString(cur.getNumber("Score").intValue()));
                    puns.add(singlePun);
                }
        } catch (ParseException e) {
            Log.d("PARSE ERROR", "-Error retrieving puns-");
        }

        return puns;
    }

    public ArrayList<ArrayList<String>> getUserPuns(String userID) {
        ArrayList<ArrayList<String>> puns = new ArrayList<ArrayList<String>>();
        ArrayList<String> singlePun;
        List<ParseObject> list;
        ParseObject object;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.whereEqualTo("UserID", userID);
        query.addDescendingOrder("Score");
        query.addAscendingOrder("createdAt");

        try {
            list = query.find();

            for (int i = 0; i < list.size(); i++) {
                singlePun = new ArrayList<String>();
                ParseObject cur = list.get(i);

                singlePun.add(cur.getString("Pun"));
                singlePun.add(Integer.toString(cur.getNumber("Score").intValue()));
                singlePun.add(getLobbyTitle(cur.getString("LobbyID")));
                singlePun.add(cur.getString("LobbyID"));

                ParseQuery<ParseObject> lobbyQuery = ParseQuery.getQuery("Lobby");
                lobbyQuery.whereEqualTo("objectId", cur.getString("LobbyID"));
                try {
                    object = lobbyQuery.getFirst();

                    singlePun.add(getUserName(object.getString("UserID")));
                    singlePun.add(object.getString("Desc"));
                    singlePun.add(format.format(object.getDate("ExpiryDate")).toString());

                } catch (ParseException e){
                    Log.d("PARSE ERROR", "-Error retrieving userPuns-");
                }


                if (!isLobbyExpired(cur.getString("LobbyID"))) {
                    puns.add(singlePun);
                }
            }

        } catch (ParseException e) {
            Log.d("PARSE ERROR", "-Error retrieving userPuns-");
        }

        return puns;
    }

    public ArrayList<ArrayList<String>> getUserTopPuns(String userID) {
        ArrayList<ArrayList<String>> puns = new ArrayList<ArrayList<String>>();
        ArrayList<String> singlePun;
        List<ParseObject> list;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Winners");
        query.whereEqualTo("AuthID", userID);
        query.addDescendingOrder("createdAt");

        try {
            list = query.find();

            for (int i = 0; i < list.size(); i++) {
                singlePun = new ArrayList<String>();
                ParseObject cur = list.get(i);

                singlePun.add(cur.getString("Pun"));
                singlePun.add(Integer.toString(cur.getNumber("Score").intValue()));
                singlePun.add(cur.getString("Theme"));

                puns.add(singlePun);
            }

        } catch (ParseException e) {
            Log.d("PARSE ERROR", "-Error retrieving userTopPuns-");
        }

        return puns;
    }

    public String getLobbyTitle(String lobbyID) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Lobby");
        ParseObject result;
        String title="";
        query.whereEqualTo("objectId", lobbyID);
        try {
            result = query.getFirst();
            title = result.getString("Theme");

        } catch (ParseException e) {
            Log.d("PARSE ERROR", "-Error retrieving theme title-");
        }

        return title;
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

    /*
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
    */

    public boolean voterOwnsPost(String voterID, String postID){
        boolean result;
        String postOwnerID;

        ParseQuery<ParseObject> postOwner = ParseQuery.getQuery("Posts");
        try {
            ParseObject lobby = postOwner.get(postID);
            postOwnerID = lobby.getString("UserID");
        }
        catch (ParseException p){ postOwnerID = "";}

        if(postOwnerID.equals(voterID)) //voter is post creator, do not allow vote
        {result = true;}
        else {result = false;}

        return result;
    }

    public boolean voterHasAlreadyVoted(String voterID, String postID) {
        boolean result;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Votes");
        query.whereEqualTo("VoterID", voterID);
        query.whereEqualTo("PostID", postID);
        try {
            query.getFirst();
            result = true;
        } catch (ParseException p) {
            result = false;
        }
    return result;
    }


    //User votes for a post, incrementing the post score, creators score, and creates vote record
    public int voteOnPost(String voterID, String postID, String lobbyID, String authID) {
        int result;
        if(voterOwnsPost(voterID, postID)){result = 0;}
        else if (voterHasAlreadyVoted(voterID, postID)){result = 1;}
        else {

            incrementPostScore(postID);
            incrementUserScore(authID);


            //creates record of vote
            ParseObject newVote = new ParseObject("Votes");
            newVote.put("VoterID", voterID);
            newVote.put("PostID", postID);
            newVote.put("LobbyID", lobbyID);
            newVote.put("AuthID", authID);
            newVote.saveInBackground();

            result = 2;
        }

        return result;
        //result 0 = voter owned post being voted on
        //result 1 = voter already voted on post
        //result 2 = vote successful.
    }

    private void incrementPostScore(String postID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.whereEqualTo("objectId", postID);
        try {
            ParseObject post = query.getFirst();
            post.increment("Score");
            post.saveInBackground();
        }
        catch (ParseException e) {}


    }

    private void incrementUserScore(String authID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("UserID", authID);
        try {
            ParseObject user = query.getFirst();
            user.increment("Score");
            user.saveInBackground();
        }
        catch (ParseException e) {}
    }

    //removes pun from pun table
    public void deletePun(String punID)
    {
        //still working on this one
    }

}
