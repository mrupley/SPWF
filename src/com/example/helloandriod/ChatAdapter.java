package com.example.helloandriod;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter {
	
	ArrayList<String> chatList;

	protected final Context mContext;
	protected TextView incomingMessageTextView;
	public ChatAdapter(final Context pContext) {
		this.mContext = pContext;
		chatList = new ArrayList<String>();
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
			view = LayoutInflater.from(this.mContext).inflate(R.layout.incoming_message, null);
		}

		//alternates between two background images and positions based on "who said it"
		if(pPosition % 2 == 0){
			incomingMessageTextView = (TextView) view.findViewById(R.id.incomingMessage);
			incomingMessageTextView.setBackgroundResource(R.drawable.chat_bubble_player1);
			incomingMessageTextView.setText(chatList.get(pPosition));
			incomingMessageTextView.setGravity(Gravity.LEFT);
		} else {
			incomingMessageTextView = (TextView) view.findViewById(R.id.incomingMessage);
			incomingMessageTextView.setBackgroundResource(R.drawable.chat_bubble_player2);
			incomingMessageTextView.setText(chatList.get(pPosition));
			incomingMessageTextView.setGravity(Gravity.RIGHT);
		}
		
		return view;
	}

	@Override
	public int getCount() {
		return chatList.size();
	}

	@Override
	public Object getItem(int position) {
		return chatList.get(position);
	}

	public void add(String message) {
		chatList.add(message);
	}

}