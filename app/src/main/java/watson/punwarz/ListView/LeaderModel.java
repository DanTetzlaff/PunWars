package watson.punwarz.ListView;

import android.graphics.Bitmap;

/**
 * Created by Dan on 2016-02-28.
 */
public class LeaderModel {

    private Bitmap leaderImg= Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
    private String leaderName="";
    private String leaderScore="";
    private String pos="";
    private String leaderID="";
    private String leaderRank="";

    /** Setter Methods **/

    public void setLeaderName(String leaderName){
        this.leaderName = leaderName;
    }

    public void setLeaderScore(String leaderScore){ this.leaderScore = leaderScore; }

    public void setPos(String pos){ this.pos = pos; }

    public void setLeaderImg(Bitmap leaderImg){ this.leaderImg = leaderImg; }

    public void setLeaderID(String leaderID){ this.leaderID = leaderID; }

    public void setLeaderRank(String leaderRank){ this.leaderRank = leaderRank; }

    /** Getter Methods **/

    public String getLeaderName(){
        return this.leaderName;
    }

    public String getLeaderScore(){ return this.leaderScore; }

    public String getPos() { return this.pos; }

    public Bitmap getLeaderImg() { return this.leaderImg; }

    public String getLeaderID() { return this.leaderID; }

    public String getLeaderRank() { return this.leaderRank; }
}
