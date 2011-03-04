package fr.gabuzomeu.aCoincoin;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.util.Linkify;
import android.text.util.Linkify.MatchFilter;
import android.text.util.Linkify.TransformFilter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MessageAdapterView extends LinearLayout {        
       
	Context aContext;
	
	public MessageAdapterView(Context context,  final CoinCoinMessage message ) {
            super( context );
            aContext = context;
	    this.setBackgroundColor( Color.WHITE);
	   
            this.setOrientation(HORIZONTAL);        
	    
            int color = Color.parseColor( message.getBackgroundColor());
            
            LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(50, LayoutParams.FILL_PARENT);
            
            timeParams.setMargins(1, 1, 1, 1);
            TextView timeControl = new TextView( context );
            String sClock= String.valueOf( message.getTime());
            final String hour=sClock.substring( 8, 10);
            final String minutes=sClock.substring(10,12);
            final String seconds=sClock.substring( 12,14);
            
            String sLogin;
            if( message.getLogin() == null || message.getLogin().length() <= 0 )
            	sLogin = message.getInfo() ;
            else
            	sLogin =  message.getLogin() ;
            
            timeControl.setText( hour + ":" + minutes + ":" + seconds + "\n" + sLogin);
            timeControl.setTextSize(10f);
            timeControl.setTextColor(Color.RED );
            timeControl.setBackgroundColor( color );
            addView( timeControl, timeParams);      
	   
            OnClickListener onNorlogeClickListener = new OnClickListener() {
				
				public void onClick(View v) {
					
				//	CoincoinPalmipede pinni = new CoincoinPalmipede( this, (CoinCoinApp)context.getApplicationContext().  )
					
					Intent intent = new Intent( aContext , CoincoinPostActivity.class);
					intent.putExtra("board", message.getBoardId());
					Log.i( CoinCoinApp.LOG_TAG, "SEND BOARD ID : " + message.getBoardId()  );
					intent.putExtra("norloge", hour + ":" + minutes +":" + seconds +" ");
					aContext.startActivity( intent);
					
				}
			};
            
			timeControl.setOnClickListener( onNorlogeClickListener);
	    
	    //Message
            
            
            LinearLayout.LayoutParams messageParams = new LinearLayout.LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            messageParams.setMargins(1, 1, 1, 1);
            messageParams.weight=1;
            TextView messageControl = new TextView(context);
            //WebView messageControl = new WebView( context);
            
            String transformedMessage = message.getMessage();
            
            transformedMessage= transformedMessage.replaceAll( "<a href=\"" , " ");
            transformedMessage= transformedMessage.replaceAll( "\">.*\\[.*\\]*.</a>" , " ");
            
            
            OnClickListener messageOnClickListener = new OnClickListener() {
				
				public void onClick(View v) {
					TextView clicked = ( TextView)v;
					Log.i( CoinCoinApp.LOG_TAG, "CLICK: " + clicked.getText());
 
				}
			};
            
            
		          
            
            messageControl.setTextColor(Color.BLACK);
            messageControl.setBackgroundColor( color );
            messageControl.setFocusable(true);
            messageControl.setClickable( true);
            messageControl.setLinksClickable( true);
            messageControl.setOnClickListener( messageOnClickListener);    
           
           
            
           Spanned str = spannabalizeMessage( transformedMessage);
           
            messageControl.append( str );
           
			
			Linkify.addLinks( messageControl, Linkify.ALL);
			
			
            addView( messageControl, messageParams); 
        }


       


		public Spanned spannabalizeMessage( String _str){
        	SpannableString str = new SpannableString( _str);
        	
        	Spanned span = Html.fromHtml( _str);
          	return span;
        }

        
        
        
       





}


public class MessageAdapter extends ArrayAdapter<CoinCoinMessage> {

    private Context context;
    private List<CoinCoinMessage> messagesList;

    
    
    
    public MessageAdapter(Context context, int viewId, List<CoinCoinMessage> messagesList ) { 
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
        CoinCoinMessage message = this.getItem( position);
      ///  Log.i( CoinCoinApp.LOG_TAG, "AJOUT ELEMENT VIEW: " + message);
        
        return new MessageAdapterView(  this.context, message );
        
    }

}
