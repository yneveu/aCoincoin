package fr.gabuzomeu.aCoincoin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CoincoinPostActivity extends Activity {

	
	RadioGroup boardsList;
	TextView msg;
	Button postButton;
	
	String message;
	CoinCoinApp app;
	CoincoinBoard postBoard;
	
	Context acontext;
	
	public void setApp(CoinCoinApp app) {
		this.app = app;
	}

	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		acontext = this;
		app = (CoinCoinApp)getApplication();

		setContentView(R.layout.palmipede);
		boardsList = (RadioGroup)findViewById( R.id.RadioGroup01);
		
		postButton = ( Button)findViewById( R.id.Button01);
		msg = (TextView)findViewById( R.id.EditText01);
		
		
		Bundle extras = getIntent().getExtras();
		
		if( extras != null){
			String  norloge = extras.getString( "norloge");
			int boardId = extras.getInt("board");
		
		if( boardId >=0 ){
			boardsList.setVisibility(8);
			postBoard = app.getBoardById( boardId );
		}
		
		boardsList.check( boardId);
		
		if( norloge != null){
			msg.setText( norloge);
			
		}
		}
		
		
		/*Catch enter key to post*/
		msg.setOnKeyListener(new OnKeyListener(){
		    public boolean onKey(View v, int keyCode, KeyEvent event)
		    {
		        if (event.getAction() == KeyEvent.ACTION_DOWN)
		        {
		            switch (keyCode)
		            {
		                case KeyEvent.KEYCODE_DPAD_CENTER:
		                case KeyEvent.KEYCODE_ENTER:
		                	if( postBoard != null){
		                    	postMessage( msg.getText().toString() );
		                    	Intent svc = new Intent( acontext, ICoincoinService.class);
		                    	app.startService(svc);
		                    	finish();
		                    }
		                    return true;
		                default:
		                    break;
		            }
		        }
		        return false;
		    }

			
		});

		
		
		
		
		
		postButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                message = new String( msg.getText().toString() );
                
                if( postBoard != null){
                	postMessage( msg.getText().toString() );
                	Intent svc = new Intent( acontext, ICoincoinService.class);
                	app.startService(svc);
                	finish();
                }
                else{
    	        	Toast toast = Toast.makeText( acontext , "Please choose a board...", Toast.LENGTH_SHORT);
                	toast.show();
                }
                
            }
		});
		
	;
	
	final ArrayList<CoincoinBoard> aboardsList = app.getBoardList();
	//BoardAdapter adapter = new BoardAdapter( context , aboardsList );
	
	for ( int i = 0; i < aboardsList.size(); i++){
		RadioButton button = new RadioButton(acontext);
		button.setText( aboardsList.get(i).getName() );
		button.setId( i);
		boardsList.addView( button );
	}
	
	
	RadioGroup.OnCheckedChangeListener l = new RadioGroup.OnCheckedChangeListener() {
		
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			
			Log.i( CoinCoinApp.LOG_TAG, "Checked-------->  " + aboardsList.get( checkedId).getName());
			
			postBoard = aboardsList.get( checkedId);
		}
	};
	
	boardsList.setOnCheckedChangeListener(l);

	
	ImageButton buttonBold = (ImageButton)findViewById( R.id.ImageButtonBold);
	buttonBold.setImageResource( R.drawable.bold);
	
	OnClickListener boldClickListener = new OnClickListener() {
		
		public void onClick(View v) {
			int start = msg.getSelectionStart();
			int end = msg.getSelectionEnd();
			
			String mess = msg.getText().toString();
			Log.i( CoinCoinApp.LOG_TAG, "Mess-> " + mess + "Start->  " + start + " End-> " + end);
			msg.setText( mess.subSequence(0, start) + "<b>" + mess.subSequence(start , end) + "</b>" + mess.subSequence( end, mess.length()-1));
			
			
			//msg.append( "<b>", start, start + 3);
			//msg.append("</b>", end, end + 4);
			
			
		}
	};	
	buttonBold.setOnClickListener( boldClickListener);
	
	
	ImageButton buttonItalic = (ImageButton)findViewById( R.id.ImageButtonItalic);
	buttonItalic.setImageResource( R.drawable.italic);
	
	ImageButton buttonUnderlined = (ImageButton)findViewById( R.id.ImageButtonUnderlined);
	buttonUnderlined.setImageResource( R.drawable.underline);
	
	ImageButton buttonStroke = (ImageButton)findViewById( R.id.ImageButtonStroke);
	buttonStroke.setImageResource( R.drawable.stroke);
	
	
	}
		
		private boolean postMessage( String message){
			String utf8Message;
			 try {
				utf8Message = new String( message.getBytes( "UTF-8") );
			} catch (UnsupportedEncodingException e1) {
				utf8Message = message;
				Log.i( CoinCoinApp.LOG_TAG, "!!!!!!!!!!!!!!!!!!!!!!!Content encoding: " + e1.getMessage());
			}
		      
			message=utf8Message;
			
	        HttpPost post = new HttpPost( postBoard.getPostUrl());
	         
            post.addHeader(new BasicHeader("Cookie", postBoard.getCookies() ));
	        post.addHeader(new BasicHeader("Referer", postBoard.getPostReferer()));
	        post.addHeader(new BasicHeader("Host", postBoard.getHost()));
	        post.addHeader(new BasicHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
	        post.addHeader(new BasicHeader("Accept-Language","fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3"));
	        post.addHeader(new BasicHeader("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.7"));
	        post.addHeader(new BasicHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8"));
	        
	        if( postBoard.getUserAgent() .compareTo("_default_") == 0 )
	        	post.setHeader("User-Agent", "ACoincoin " + CoinCoinApp.getVERSION() );
	        else
	        	post.setHeader("User-Agent", postBoard.getUserAgent() );
  
	        HttpClient hClient = new DefaultHttpClient(); 
	              	         
	        try {
	               List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	                       
	              
	               nameValuePairs.add( new BasicNameValuePair( postBoard.getExtraPostParams(), message));
	               nameValuePairs.add( new BasicNameValuePair( "message", message));
	      	      
	               UrlEncodedFormEntity ent = new UrlEncodedFormEntity(nameValuePairs ,HTTP.UTF_8 );
	               post.setEntity( ent);
	               
	               HttpResponse response;
	               response=hClient.execute( post);
	               Log.i( CoinCoinApp.LOG_TAG, "RESPONSE-----------> " + response.getStatusLine());
	               Log.i( CoinCoinApp.LOG_TAG, "RESPONSE-----------> " + response.getStatusLine().getReasonPhrase());
	               
	               return true;
	          } catch (ClientProtocolException e) {
	               e.printStackTrace();
	               return false;
	          } catch (IOException e) {
	               e.printStackTrace();
	               return false;
	          } 
	          
	          
	          
		}
		
		

		
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
