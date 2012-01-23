package fr.gabuzomeu.aCoincoin;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spanned;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MessageAdapter extends ArrayAdapter<CoinCoinMessage> {

    private Context context;
    private ArrayList<CoinCoinMessage> messagesList;

    
    
    
    public MessageAdapter(Context context, int viewId, ArrayList<CoinCoinMessage> messagesList ) { 
       super( context, viewId, messagesList);
    	this.context = context;
        this.messagesList = messagesList;
               
    }

    public int getCount() {                        
        return messagesList.size();
    }

    public CoinCoinMessage getItem(int position) {     
        return messagesList.get(position);
    }

    public long getItemId(int position) {  
        return position;
    }
    

    public View getView(int position, View convertView, ViewGroup parent) { 
    	    	
    	int nbMessages = messagesList.size();
    	final CoinCoinMessage message = messagesList.get( nbMessages - ( position + 1));
    	
    	
    	View v = convertView;
 	
    	
    	if( v == null){
    		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
    		v = inflater.inflate( R.layout.messagerow, null);
    	}
    	
    	   	
    	
    	TextView login = (TextView)v.findViewById( R.id.login);
    	String sLogin;
    	if( message.getLogin() == null || message.getLogin().length() <= 0 ){
        	sLogin = message.getInfo() ;
        	login.setTypeface( Typeface.defaultFromStyle(Typeface.ITALIC),Typeface.ITALIC );
    	}
        else
        	sLogin =  message.getLogin() ;
    	login.setText( sLogin);
    	
		
		
		String sClock= String.valueOf( message.getTime());
        final String hour=sClock.substring( 8, 10);
        final String minutes=sClock.substring(10,12);
        final String seconds=sClock.substring( 12,14);
        String cNorloge =  hour + ":" + minutes + ":" + seconds;
        
        TextView norloge =  (TextView)v.findViewById( R.id.norloge);
		
		
		OnClickListener onNorlogeClickListener = new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent( context , CoincoinPostActivity.class);
				intent.putExtra("board", message.getBoardId());
				Log.i( CoinCoinApp.LOG_TAG, "SEND BOARD ID : " + message.getBoardId()  );
				intent.putExtra("norloge", hour + ":" + minutes +":" + seconds +" ");
				context.startActivity( intent);
			}
		};
		
		norloge.setOnClickListener( onNorlogeClickListener);
		
		norloge.setText( cNorloge);
		
    	
		
		//Traitement du message
		TextView post =  (TextView)v.findViewById( R.id.post);
        String transformedMessage = message.getMessage();
        transformedMessage= transformedMessage.replaceAll( "<a href=\"" , " ");
        transformedMessage= transformedMessage.replaceAll( "\">.*\\[.*\\]*.</a>" , " ");
        
        OnClickListener messageOnClickListener = new OnClickListener() {
			
			public void onClick(View v) {
				TextView clicked = ( TextView)v;
				Log.i( CoinCoinApp.LOG_TAG, "CLICK: " + clicked.getText());

			}
		};
        
        
        post.setFocusable(true);
        post.setClickable( true);
        post.setLinksClickable( true);
        post.setOnClickListener( messageOnClickListener);    
       
        Spanned span = Html.fromHtml(transformedMessage);
        post.setText( span);
        Linkify.addLinks( post, Linkify.ALL);
		
		int color=1;
		if( message.getBackgroundColor() != null)
			color = Color.parseColor( message.getBackgroundColor());
		
		LinearLayout postLayout =  (LinearLayout)v.findViewById( R.id.infos);
		postLayout.setBackgroundColor( color);
		LinearLayout messageLayout =  (LinearLayout)v.findViewById( R.id.message);
		messageLayout.setBackgroundColor( color);
		
		login.setBackgroundColor( color);
		post.setBackgroundColor( color);
		norloge.setBackgroundColor( color);
		
		
		login.setTextColor( Color.RED);
		post.setTextColor( Color.BLACK);
		norloge.setTextColor( Color.RED);
		
    	return v;
    		 	

    }

}
