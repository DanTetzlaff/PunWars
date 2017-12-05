package watson.punwarz.ListView;

import android.graphics.Bitmap;

/**
 * @author Daniel Tetzlaff
 * @version 1.0
 * Created: 2017-12-01
 */

public class FriendModel
{
    private Bitmap friendImg=Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
    private String friendName="";
    private String friendScore="";
    private String friendID="";

    /** Setter Methods **/

    public void setFriendImg(Bitmap friendImg) { this.friendImg = friendImg; }

    public void setFriendName(String friendName) { this.friendName = friendName; }

    public void setFriendScore(String friendScore) { this.friendScore = friendScore; }

    public void setFreindID(String friendID) { this.friendID = friendID; }

    /** Getter Methods **/

    public Bitmap getFriendImg() { return this.friendImg; }

    public String getFriendName() { return this.friendName; }

    public String getFriendScore() { return this.friendScore; }

    public String getFriendID() { return this.friendID; }
}
