package watson.punwarz;

/**
 * @author Daniel Tetzlaff
 * @version 1.0
 * 
 * Created: by 2017-11-30
 * 
 * Description: Contains methods for retrieving string classifications/names based on user points
 */

public class Ranks 
{
    public String getRankName(int points)
    {
        String rank = "";

        if (points < 30){ rank = "Punching Bag"; }
        else if (points < 60){ rank = "Pun'kd"; }
        else if (points < 100){ rank = "Depundable"; }
        else if (points < 140){ rank = "Pungent"; }
        else if (points < 190){ rank = "Puntagon"; }
        else if (points < 240){ rank = "Punny"; }
        else if (points < 300){ rank = "Punctual"; }
        else if (points < 360){ rank = "Puntastic"; }
        else if (points < 430){ rank = "Punchier"; }
        else if (points < 500){ rank = "Punderful"; }
        else if (points < 580){ rank = "Cyberpun"; }
        else { rank = "Punisher"; }

        return rank;
    }
}
