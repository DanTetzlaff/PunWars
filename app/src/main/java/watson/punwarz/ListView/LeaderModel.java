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

    /** Setter Methods **/

    public void setLeaderName(String leaderName){
        this.leaderName = leaderName;
    }

    public void setLeaderScore(String leaderScore){ this.leaderScore = leaderScore; }

    public void setPos(String pos){ this.pos = pos; }

    public void setLeaderImg(Bitmap leaderImg){ this.leaderImg = leaderImg; }

    /** Getter Methods **/

    public String getLeaderName(){
        return this.leaderName;
    }

    public String getLeaderScore(){ return this.leaderScore; }

    public String getPos() { return this.pos; }

    public Bitmap getLeaderImg() {return this.leaderImg; }
}
