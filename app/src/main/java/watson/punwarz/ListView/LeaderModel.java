package watson.punwarz.ListView;

/**
 * Created by Dan on 2016-02-28.
 */
public class LeaderModel {

    private String leaderImage="";
    private String leaderName="";
    private String leaderScore="";
    private String pos="";

    /** Setter Methods **/

    public void setLeaderImage(String leaderImage){
        this.leaderImage = leaderImage;
    }

    public void setLeaderName(String leaderName){
        this.leaderName = leaderName;
    }

    public void setLeaderScore(String leaderScore){ this.leaderScore = leaderScore; }

    public void setPos(String pos){ this.pos = pos; }

    /** Getter Methods **/

    public String getLeaderImage(){
        return this.leaderImage;
    }

    public String getLeaderName(){
        return this.leaderName;
    }

    public String getLeaderScore(){ return this.leaderScore; }

    public String getPos() { return this.pos; }
}
