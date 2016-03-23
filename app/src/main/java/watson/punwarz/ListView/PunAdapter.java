package watson.punwarz.ListView;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import watson.punwarz.Lobby;
import watson.punwarz.Puns;
import watson.punwarz.R;

import java.util.ArrayList;

/**
 * Created by Dan on 2016-03-01.
 */
public class PunAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    PunModel tempValues=null;
    int i=0;

    public PunAdapter(Activity a, ArrayList d, Resources resLocal){
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

    public Object getItem(int position) { return position; }

    public long getItemId(int position) { return position; }

    public static class ViewHolder {
        public TextView punAuthText;
        public TextView punText;
        public TextView punVotesText;

    }

    public void upVote(View v){
        Log.v("CustomAdapter", "=-=-=Row button clicked=-=-=");
    }

    private class OnItemClickListener implements View.OnClickListener{
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0){

            Puns sct = (Puns)activity;

            sct.onItemClick(mPosition);
        }
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){
            vi = inflater.inflate(R.layout.punitem, null);

            holder = new ViewHolder();
            holder.punAuthText = (TextView) vi.findViewById(R.id.punAuth);
            holder.punText = (TextView) vi.findViewById(R.id.pun);
            holder.punVotesText = (TextView) vi.findViewById(R.id.punVotes);

            vi.setTag( holder );
        }
        else {
            holder=(ViewHolder)vi.getTag();
        }

        if(data.size()<=0)
        {
            holder.punText.setText("No Data");
        }
        else
        {
            tempValues=null;
            tempValues = ( PunModel ) data.get( position );

            holder.punAuthText.setText( tempValues.getPunAuth() );
            holder.punText.setText( tempValues.getPun() );
            holder.punVotesText.setText( tempValues.getPunVotes() );

        }

        return vi;
    }


}
