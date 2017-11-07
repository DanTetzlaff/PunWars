package watson.punwarz.ListView;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;

import java.util.ArrayList;

import watson.punwarz.Leaderboard;
import watson.punwarz.R;

/**
 * Created by Dan on 2017-11-07.
 */
public class CustomLeaderAdapter extends BaseAdapter implements View.OnClickListener{

    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    LeaderModel tempValues=null;
    int i=0;

    public CustomLeaderAdapter(Activity a, ArrayList d, Resources resLocal){
        activity = a;
        data = d;
        res = resLocal;

        inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount(){
        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position){
        return position;
    }

    public long getItemId(int position){
        return position;
    }

    public static class ViewHolder {

        public TextView leaderName;
        public TextView score;
        public TextView pos;
        public ImageView leaderImg;

    }

    public View getView(int position, View convertView, ViewGroup parent){

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){
            vi = inflater.inflate(R.layout.leaderboarditem, null);

            holder = new ViewHolder();
            holder.leaderName = (TextView) vi.findViewById(R.id.leaderName);
            holder.score = (TextView) vi.findViewById(R.id.leaderScore);
            holder.pos = (TextView) vi.findViewById(R.id.pos);
            holder.leaderImg = (ImageView) vi.findViewById(R.id.leaderImage);

            vi.setTag( holder );
        }
        else {
            holder=(ViewHolder)vi.getTag();
        }

        if (data.size()<=0)
        {
            holder.leaderName.setText("No Users");
        }
        else
        {
            tempValues=null;
            tempValues = ( LeaderModel ) data.get( position );

            holder.leaderName.setText( tempValues.getLeaderName() );
            holder.score.setText( tempValues.getLeaderScore() );
            holder.pos.setText( tempValues.getPos() );
            //holder.leaderImg.( tempValues.getTopPun() );

            vi.setOnClickListener(new OnItemClickListener( position ));
        }

        if (position % 2 == 1) {
            vi.setBackgroundColor(Color.parseColor("#d5d3d3"));
        } else {
            vi.setBackgroundColor(Color.parseColor("#ffa147"));
        }

        return vi;
    }

    @Override
    public void onClick(View v){
        Log.v("CustomLeaderAdapter", "=-=-=Row button clicked=-=-=");
    }

    private class OnItemClickListener implements View.OnClickListener{
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0){

            Leaderboard sct = (Leaderboard) activity;

            sct.onLeaderItemClick(mPosition);
        }
    }
}
