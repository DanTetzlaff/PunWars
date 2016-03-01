package com.example.watson.punwarz.ListView;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.app.*;
import android.widget.TextView;

import com.example.watson.punwarz.Lobby;
import com.example.watson.punwarz.R;

import java.util.*;

/**
 * Created by Dan on 2016-02-28.
 */
public class CustomAdapter extends BaseAdapter implements View.OnClickListener{

    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    ListModel tempValues=null;
    int i=0;

    public CustomAdapter(Activity a, ArrayList d, Resources resLocal){
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

        public TextView titleText;
        public TextView authorText;
        public TextView themeText;
        public TextView expireDate;
        public TextView topPun;

    }

    public View getView(int position, View convertView, ViewGroup parent){

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){
            vi = inflater.inflate(R.layout.tabitem, null);

            holder = new ViewHolder();
            holder.titleText = (TextView) vi.findViewById(R.id.lobbyTitle);
            holder.authorText = (TextView) vi.findViewById(R.id.lobbyAuthor);
            holder.themeText = (TextView) vi.findViewById(R.id.lobbyTheme);
            holder.expireDate = (TextView) vi.findViewById(R.id.expireDate);
            holder.topPun = (TextView) vi.findViewById(R.id.topPun);

            vi.setTag( holder );
        }
        else {
            holder=(ViewHolder)vi.getTag();
        }

        if (data.size()<=0)
        {
            holder.titleText.setText("No Data");
        }
        else
        {
            tempValues=null;
            tempValues = ( ListModel ) data.get( position );

            holder.titleText.setText( tempValues.getLobbyTitle() );
            holder.authorText.setText( tempValues.getLobbyAuthor() );
            holder.themeText.setText( tempValues.getLobbyTheme() );
            holder.expireDate.setText( tempValues.getExpireDate() );
            holder.topPun.setText( tempValues.getTopPun() );

            vi.setOnClickListener(new OnItemClickListener( position ));
        }
        return vi;
    }

    @Override
    public void onClick(View v){
        Log.v("CustomAdapter", "=-=-=Row button clicked=-=-=");
    }

    private class OnItemClickListener implements View.OnClickListener{
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0){

            Lobby sct = (Lobby)activity;

            sct.onItemClick(mPosition);
        }
    }
}
