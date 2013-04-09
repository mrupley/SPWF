package com.example.helloandriod;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FriendAdapter extends BaseAdapter {
	
	ArrayList<String> friendList;

	protected final Context mContext;
	
	protected TextView incomingMessageTextView;
	protected TextView numberIconTextView;
	protected TextView gameTitleWithTextView;
	protected TextView gameTitleDateTextView;
	protected TextView gameTitleLastPlayedTextView;
	
	public FriendAdapter(final Context pContext) {
		this.mContext = pContext;
		friendList = new ArrayList<String>();
		
	}

	@Override
	public long getItemId(final int pPosition) {
		return pPosition;
	}
	
	@Override
	public View getView(final int pPosition, final View pConvertView, final ViewGroup pParent) {
		final View view;
		if (pConvertView != null) {
			view = pConvertView;
		} else {
			view = LayoutInflater.from(this.mContext).inflate(R.layout.friends_view, null);
		}

		//Setting the Letter
		incomingMessageTextView = (TextView) view.findViewById(R.id.incomingMessage);
		incomingMessageTextView.setText(friendList.get(pPosition).substring(0, 1).toUpperCase(Locale.US));
        
        //Setting the number value for the letter
		numberIconTextView = (TextView) view.findViewById(R.id.numberIcon);
		numberIconTextView.setText(GameTile.friendValues.get(friendList.get(pPosition).substring(0, 1).toUpperCase()));
        
		gameTitleWithTextView = (TextView) view.findViewById(R.id.gameTitleWith);
		gameTitleWithTextView.setText(mContext.getResources().getString(R.string.app_name) + " with " + friendList.get(pPosition));
        
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        
        gameTitleDateTextView = (TextView) view.findViewById(R.id.gameTitleDate);
        gameTitleDateTextView.setText(currentDateTimeString);

        gameTitleLastPlayedTextView = (TextView) view.findViewById(R.id.gameTitleLastPlayed);
        gameTitleLastPlayedTextView.setText("Last Played " + pPosition  + " day(s) ago.");
        
        //TextView testTextView6 = (TextView) view.findViewById(R.id.gameVersion);
        //testTextView6.setText("@strings/game_name" + " " + MainActivity.version);
        
		return view;
	}

	@Override
	public int getCount() {
		return friendList.size();
	}

	@Override
	public Object getItem(int position) {
		return friendList.get(position);
	}

	public void add(String message) {
		friendList.add(message);
	}

}