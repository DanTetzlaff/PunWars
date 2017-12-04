package watson.punwarz;

import android.app.Application;
import android.util.Log;

import com.parse.*;
import com.parse.ParseException;

import java.text.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//TODO remove unused statements
//TODO FULL DOCUMENTATION OF CLASS
public class ParseApplication extends Application {

    private boolean exists = false; //all-purpose boolean for query results.
    private SimpleDateFormat format = new SimpleDateFormat("MMM d, ''yy");

    @Override
    public void onCreate()
    {
        super.onCreate();

        Parse.initialize(this, getResources().getString(R.string.parse_app_id), getResources().getString(R.string.parse_client_id));
    }

    //creates a new user object in Parse if the user has not previously used app.
    public void createNewUser(String facebookID, String displayName)
    {
        ParseObject newUser = new ParseObject("Users");
        newUser.put("UserID", facebookID);
        newUser.put("DisplayName", displayName);
        newUser.put("Score", 0);
        newUser.put("ProfilePictureBy", false);
        newUser.put("ProfilePictureID", 0);
        newUser.saveInBackground();
    }

    //creates a new lobby associated with the active user
    public void createNewLobby(String userID, String lobbyTheme, String lobbyDesc, Date expiryDate)
    {
        ParseObject newLobby = new ParseObject("Lobby");
        newLobby.put("Theme", lobbyTheme);
        newLobby.put("Desc", lobbyDesc);
        newLobby.put("UserID", userID);
        newLobby.put("ExpiryDate", expiryDate);
        newLobby.saveInBackground();
    }

    /**
     * @param userID ID of the user that submitted a pun (post), is the author
     * @param lobbyID ID of the lobby/theme that the pun is submitted to
     * @param punText Content of the post, or the "Pun itself"
     *
     * Description: creates a new Post associated to a particular Lobby
     */
    public void createNewPun(String userID, String lobbyID, String punText)
    {
            ParseObject newPost = new ParseObject("Posts");
            newPost.put("UserID", userID);
            newPost.put("LobbyID", lobbyID);
            newPost.put("Pun", punText);
            newPost.put("Score", 0);
            newPost.saveInBackground();
    }

    /**
     * @param requestFromID userID for the user that made the request, in friend-one (friend-from) position
     * @param requestToID userID for the user that request is being made to, in friend-two (friend-to) position
     *
     * Description: creates a friend request
     */
    public void createFriendRequest(String requestFromID, String requestToID)
    {
        ParseObject newFriendRequest = new ParseObject("FriendRequests");
        newFriendRequest.put("RequestFromID", requestFromID);
        newFriendRequest.put("RequestToID", requestToID);
        newFriendRequest.saveInBackground();
    }

    /**
     * @param friendOneID userID for the user in friend-one position
     * @param friendTwoID userID for the user in friend-two position
     *
     * Description: creates friend relation
     */
    public void createFriendRelation(String friendOneID, String friendTwoID)
    {
        ParseObject newFriends = new ParseObject("FriendPairs");
        newFriends.put("FriendOneID", friendOneID);
        newFriends.put("FriendTwoID", friendTwoID);
        newFriends.saveInBackground();
    }

    //remove friend request
    public void removeFriendRequest(String requestFromID, String requestToID) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequests");
        String[] tempIDsList = {requestFromID, requestToID};

        query.whereContainedIn("RequestFromID", Arrays.asList(tempIDsList));
        query.whereContainedIn("RequestToID", Arrays.asList(tempIDsList));
        try {
            ParseObject requestToRemove = query.getFirst();
            requestToRemove.delete();

        } catch (ParseException e) {}
    }

    //remove a friendship
    //TODO test if this works
    public void removeFriendship(String friendOneID, String friendTwoID)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendPairs");
        String[] tempIDsList = {friendOneID, friendTwoID};

        query.whereContainedIn("FriendOneID", Arrays.asList(tempIDsList));
        query.whereContainedIn("FriendTwoID", Arrays.asList(tempIDsList));
        try {
            ParseObject friendshipToRemove = query.getFirst();
            friendshipToRemove.delete();
        } catch (ParseException e) {}
    }

    /**
     * @param friendOneID userID for the user in friend-one position
     * @param friendTwoID userID for the user in friend-two position
     * @return boolean in reference to if or if-not a friendship exists in db
     *
     * Description: check if friendship exists
     */
    public boolean doesFriendshipExist(String friendOneID, String friendTwoID)
    {
        boolean exist;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendPairs");
        String[] tempIDsList = {friendOneID, friendTwoID};

        query.whereContainedIn("FriendOneID", Arrays.asList(tempIDsList));
        query.whereContainedIn("FriendTwoID", Arrays.asList(tempIDsList));
        try {
            query.getFirst();
            exist = true;
        } catch (ParseException p) {
            exist = false;
        }

        return exist;
    }

    /**
     * @param friendOneID userID for the user in friend-one (request-from) position
     * @param friendTwoID userID for the user in friend-two (request-to) position
     * @return boolean in reference to if or if-not a friend request has been made but not yet accepted
     *
     * Description: check if general friend request is outstanding
     */
    public boolean doesFriendRequestExist(String friendOneID, String friendTwoID)
    {
        boolean exist;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequests");
        String[] tempIDsList = {friendOneID, friendTwoID};

        query.whereContainedIn("RequestFromID", Arrays.asList(tempIDsList));
        query.whereContainedIn("RequestToID", Arrays.asList(tempIDsList));
        try {
            query.getFirst();
            exist = true;
        } catch (ParseException p) {
            exist = false;
        }

        return exist;
    }

    /**
     * @param friendFromID userID of friend that made request
     * @param friendToID userID of friend that request is made to
     * @return boolean in reference to if or if-not a friend request exists and is not accepted yet
     *
     * Description: check if friend request TO exists
     */
    public boolean doesFriendRequestToExist(String friendFromID, String friendToID)
    {
        boolean exist;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequests");

        query.whereEqualTo("RequestFromID", friendFromID);
        query.whereEqualTo("RequestToID", friendToID);
        try {
            query.getFirst();
            exist = true;
        } catch (ParseException e) { exist = false; }

        return exist;
    }

    /**
     *
     * @param userID is the user being queried
     * @return result if the number of friends given user has
     *
     * Description: count number of friends for a given user
     */
    public int countUserFriends(String userID)
    {
        int result = 0;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendPairs");
        query.whereEqualTo("FriendOneID", userID);
        query.whereEqualTo("FriendTwoID", userID);

        try{
            result = query.count();
        } catch (ParseException e) {}

        return result;
    }

    /**
     * @param userID is the user being queried
     * @return result of number of friend requests given user has
     *
     * Description: count number of friend requests for a given user
     */
    public int countUserFriendRequests(String userID)
    {

        int result = 0;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequests");
        query.whereEqualTo("RequestToID", userID);

        try{
            result = query.count();
        } catch (ParseException e) {}

        return result;
    }

    /**
     * @param userID is the user to be queried for friend requests
     * @return a list of all current friend requests that can be accepted by user
     *
     * Decsription: return information to display name and link to requesting user profile
     */
    public ArrayList<ArrayList<String>> getFriendRequests(String userID)
    {
        ArrayList<ArrayList<String>> friendRequests = new ArrayList<ArrayList<String>>();
        ArrayList<String> singleFriendRequest = new ArrayList<String>();
        List<ParseObject> list;

        ParseQuery<ParseObject> queryUserRequests = ParseQuery.getQuery("FriendRequests");
        queryUserRequests.whereEqualTo("FriendToID", userID);
        try {
            list = queryUserRequests.find();

            for (int i = 0; i < list.size(); i++)
            {
                singleFriendRequest = new ArrayList<String>();
                ParseObject currentRequest = list.get(i);

                ParseQuery<ParseObject> queryCurrentRequest = ParseQuery.getQuery("Users");
                queryCurrentRequest.whereEqualTo("UserID", currentRequest.getString("FriendFromID"));
                try {
                    ParseObject requestUserDetails = queryCurrentRequest.getFirst();
                    singleFriendRequest.add(requestUserDetails.getString("UserID"));        //pos 0
                    singleFriendRequest.add(requestUserDetails.getString("DisplayName"));   //pos 1

                    friendRequests.add(singleFriendRequest);
                } catch (ParseException e) {}
            }
        } catch (ParseException e) {}

        return friendRequests;
    }

    /**
     * @param numNeeded number of friends to retrieve in this chunk
     * @param numSkipped number of friends already skipped in this instance
     * @param userID the user being queried for friends
     * @return an ArrayList containing an ArrayList of String values about the friendship
     *
     * Description: return list of friends for a user
     */
    public ArrayList<ArrayList<String>> getFriends(int numNeeded, int numSkipped, String userID)
    {
        ArrayList<ArrayList<String>> friends = new ArrayList<ArrayList<String>>();
        ArrayList<String> singleFriend = new ArrayList<String>();
        List<ParseObject> list;

        ParseQuery<ParseObject> queryUserFriends = ParseQuery.getQuery("FriendPairs");
        queryUserFriends.setLimit(numNeeded);
        queryUserFriends.setSkip(numSkipped);

        queryUserFriends.whereEqualTo("FriendOneID", userID);
        queryUserFriends.whereEqualTo("friendTwoID", userID);
        try {
            list = queryUserFriends.find();

            for (int i = 0; i < list.size(); i++)
            {
                singleFriend = new ArrayList<String>();
                ParseObject currentFriend = list.get(i);

                ParseQuery<ParseObject> queryCurrentFriend = ParseQuery.getQuery("Users");
                if (currentFriend.getString("FriendOneID").equals(userID))
                {
                    queryCurrentFriend.whereEqualTo("UserID", currentFriend.getString("FriendTwoID"));
                }
                else
                {
                    queryCurrentFriend.whereEqualTo("UserID", currentFriend.getString("FriendOneID"));
                }

                try {
                    ParseObject friendDetails = queryCurrentFriend.getFirst();
                    singleFriend.add(friendDetails.getString("DisplayName"));           //pos 0
                    singleFriend.add(Integer.toString(friendDetails.getInt("Score")));  //pos 1
                    singleFriend.add(friendDetails.getString("UserID"));                //pos 2
                    singleFriend.add(friendDetails.getString("ProfilePictureID"));      //pos 3

                    friends.add(singleFriend);
                } catch (ParseException e) {}
            }
        } catch (ParseException e) {}


        return friends;
    }

    public boolean doesPunExist(String punText)
    {
        boolean exist;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.whereEqualTo("Pun", punText);
        try {
            query.getFirst();
            exist = true;
        } catch (ParseException p) {
            exist = false;
        }
        return exist;
    }

    //Checks if an object from the selected tableName, in the selected tableRow, and the searchedValue already exists
    //This is the general use function.
    private boolean checkIfObjectExists(String tableName, String tableRow, String searchedValue)
    {
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
        } catch (ParseException e) {}
        return objectExists;
    }

    //Checks if a given Facebook user ID is already in the Parse database
    public boolean doesUserExist(String facebookID)
    {
        exists = false;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("UserID", facebookID);
        try {
            query.getFirst();
            exists = true;
        } catch (ParseException e) {
            exists = false;
        }


        Log.d("EXISTS#####", Boolean.toString(exists)); //debugging log
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

    public int getUserPoints(String facebookID)
    {
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

    public boolean isLobbyExpired(String lobbyID)
    {
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

    public ArrayList<ArrayList<String>> getThemes(int numNeeded, int numSkipped)
    {
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

            for (int i = 0 ; i < list.size(); i++)
            {
                singleTheme = new ArrayList<String>();
                ParseObject cur = list.get(i);

                singleTheme.add(cur.getObjectId());                                          //pos 0
                singleTheme.add(format.format(cur.getDate("ExpiryDate")).toString());   //pos 1
                singleTheme.add(cur.getString("Desc"));                                 //pos 2
                singleTheme.add(cur.getString("Theme"));                                //pos 3
                singleTheme.add(getUserName(cur.getString("UserID")));                  //pos 4
                singleTheme.add(getTopPun(cur.getObjectId()));                               //pos 5

                themes.add(singleTheme);
            }
        }
        catch (ParseException e) {
            Log.d("PARSE ERROR", "-Error retrieving Theme-");
        }

    return themes;
    }

    public ArrayList<ArrayList<String>> getUserThemes(String userID)
    {
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

            for (int i = 0; i < list.size(); i++)
            {
                singleTheme = new ArrayList<String>();
                ParseObject cur = list.get(i);

                singleTheme.add(cur.getString("Theme"));                                //pos 0
                singleTheme.add(format.format(cur.getDate("ExpiryDate")).toString());   //pos 1
                singleTheme.add(cur.getString("Desc"));                                 //pos 2
                singleTheme.add(getUserName(cur.getString("UserID")));                  //pos 3
                singleTheme.add(getUserName(cur.getString("UserID")));                  //pos 4
                singleTheme.add(cur.getObjectId());                                          //pos 5

                themes.add(singleTheme);
            }

        } catch (ParseException e) {
            Log.d("PARSE ERROR", "-Error retrieving userTheme-");
        }

        return themes;
    }

    public ArrayList<ArrayList<String>> getUserLeaderboard(int numNeeded, int numSkipped)
    {
        ArrayList<ArrayList<String>> users = new  ArrayList<ArrayList<String>>();
        ArrayList<String> singleUser = new ArrayList<String>();
        List<ParseObject> list;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.setLimit(numNeeded);
        query.setSkip(numSkipped);

        query.orderByDescending("Score");

        try {
            list = query.find();

            for (int i = 0; i < list.size(); i++)
            {
                singleUser = new ArrayList<String>();
                ParseObject cur = list.get(i);

                singleUser.add(cur.getString("DisplayName"));           //pos 0
                singleUser.add(cur.getString("ProfilePictureID"));      //pos 1
                singleUser.add(Integer.toString(cur.getInt("Score")));  //pos 2
                singleUser.add(Integer.toString(numSkipped+i+1));         //pos 3
                singleUser.add(cur.getString("UserID"));                //pos 4

                users.add(singleUser);
            }
        } catch (ParseException e) {
            Log.d("PARSE ERROR", "-Error retrieving User-");
        }

        return users;
    }

    public String getTopPun (String themeID)
    {
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

    public int countThemes()
    {
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

    public int countUsers()
    {
        int result = 0;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");

        try{
            result = query.count();
        } catch (ParseException e){
            Log.d("PARSE ERROR", "-Error retrieving User-");
        }

        return result;
    }

    public String getUserName(String userID)
    {
        String result = "";

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("UserID", userID);

        try{
            result = query.getFirst().getString("DisplayName");
        } catch (ParseException e) {
            Log.d("PARSE ERROR", "-Error retrieving name-");
        }

        return result;
    }

    public void changeUserName(String userID, String newName)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("UserID", userID);
        ParseObject tempUser;

        try{
            tempUser = query.getFirst();
            tempUser.put("DisplayName", newName);
            tempUser.saveInBackground();
        } catch (ParseException e) {
            Log.d("PARSE ERROR", "-Error retrieving user-");
        }
    }

    public void changeUserPic(String userID, int newPic)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("UserID", userID);
        ParseObject tempUser;

        try{
            tempUser = query.getFirst();
            if (newPic != 0)
            {
                tempUser.put("ProfilePictureBy", true);
            }
            else
            {
                tempUser.put("ProfilePictureBy", false);
            }
            tempUser.put("ProfilePictureID", newPic);
            tempUser.saveInBackground();
        } catch (ParseException e) {
            Log.d("PARSE ERROR", "-Error retrieving user-");
        }
    }

    public ArrayList<ArrayList<String>> getPuns(String lobbyID)
    {
        ArrayList<ArrayList<String>> puns = new ArrayList<ArrayList<String>>();
        ArrayList<String> singlePun;
        List<ParseObject> list;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.whereEqualTo("LobbyID", lobbyID);
        query.addDescendingOrder("Score");
        query.addAscendingOrder("createdAt");
        try {
                list = query.find();

                for (int i=0; i < list.size(); i++)
                {
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

    public ArrayList<ArrayList<String>> getUserPuns(String userID)
    {
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

            for (int i = 0; i < list.size(); i++)
            {
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

    public ArrayList<ArrayList<String>> getUserTopPuns(String userID)
    {
        ArrayList<ArrayList<String>> puns = new ArrayList<ArrayList<String>>();
        ArrayList<String> singlePun;
        List<ParseObject> list;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Winners");
        query.whereEqualTo("AuthID", userID);
        query.addDescendingOrder("createdAt");

        try {
            list = query.find();

            for (int i = 0; i < list.size(); i++)
            {
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

    public String getLobbyTitle(String lobbyID)
    {
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

    public boolean userPicBypass(String userID)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("UserID", userID);
        ParseObject tempUser = null;
        try {
            tempUser = query.getFirst();
        } catch (ParseException e) {
            Log.d("PARSE ERROR", "-Error retrieving user-");
        }
        if (tempUser != null ) {return tempUser.getBoolean("ProfilePictureBy");}
        else {return false;}
    }

    public int getUserPicture(String userID)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("UserID", userID);
        ParseObject tempUser = null;

        try{
            tempUser = query.getFirst();
        } catch (ParseException e) {
            Log.d("PARSE ERROR", "-Error retrieving user-");
        }
        if (tempUser != null ) {return tempUser.getInt("ProfilePictureID");}
        else {return 0;}
    }

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

    public boolean voterHasAlreadyVoted(String voterID, String postID)
    {
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
    public int voteOnPost(String voterID, String postID, String lobbyID, String authID)
    {
        int result;
        if(voterOwnsPost(voterID, postID)){result = 0;}
        else if (voterHasAlreadyVoted(voterID, postID)){result = 1;}
        else
        {
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

    private void incrementPostScore(String postID)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.whereEqualTo("objectId", postID);
        try {
            ParseObject post = query.getFirst();
            post.increment("Score");
            post.saveInBackground();
        }
        catch (ParseException e) {}


    }

    private void incrementUserScore(String authID)
    {
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
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.whereEqualTo("objectId", punID);
        try
        {
            ParseObject pun = query.getFirst();
            pun.delete();
        }
        catch (ParseException e) {}
    }

}
