package fr.gabuzomeu.aCoincoin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpConnection;
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

import android.R.anim;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import 	android.widget.TableLayout;

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
		msg = (TextView)findViewById( R.id.EditText01);
		postButton = ( Button)findViewById( R.id.Button01);
		
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
		
		
		
		
		postButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                message = new String( msg.getText().toString() );
                
                if( postBoard != null){
       
                	postMessage( msg.getText().toString() );
                	
                	//CoincoinPalmipede.this.dismiss();
                }
                else{
                	/*Toast toast = new Toast( acontext);
                	//toast.setView( CoincoinActivity.this);
                	CharSequence text = "Please select a board!";
                	toast.makeText( acontext ,text, Toast.LENGTH_SHORT);
                	AlertDialog dialog = dialog.
                	
                	dialog.
                	toast.show();*/
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
				//utf8Message = new String( message.getBytes( "ISO8859-1") );
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				utf8Message = message;
				Log.i( CoinCoinApp.LOG_TAG, "!!!!!!!!!!!!!!!!!!!!!!!Content encoding: " + e1.getMessage());
				
			}
			 
	          
			message=utf8Message;
			 
			
	          HttpPost post = new HttpPost( postBoard.getPostUrl());
	         
	          Log.i( CoinCoinApp.LOG_TAG, "POST-----------> URL ----> " + postBoard.getPostUrl());
	          
	          post.addHeader(new BasicHeader("Cookie", postBoard.getCookies() ));
	          post.addHeader(new BasicHeader("Referer", postBoard.getPostReferer()));
	          post.addHeader(new BasicHeader("Host", postBoard.getHost()));
	
	          post.addHeader(new BasicHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
	          post.addHeader(new BasicHeader("Accept-Language","fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3"));
	          post.addHeader(new BasicHeader("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.7"));
	          post.addHeader(new BasicHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8"));
	          post.setHeader("User-Agent", postBoard.getUserAgent() );
	          
	          
	          
	          
	          HttpClient hClient = new DefaultHttpClient(); 
	         
	          Log.i( CoinCoinApp.LOG_TAG, "Default Charset" + Charset.defaultCharset().displayName());
	         
	          try {
	               List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	                       
	              
	               nameValuePairs.add( new BasicNameValuePair( postBoard.getExtraPostParams(), message));
	               nameValuePairs.add( new BasicNameValuePair( "message", message));
	               //nameValuePairs.add( new BasicNameValuePair("section", "1"));
	               //nameValuePairs.add( new BasicNameValuePair("board[object_type]", "Free"));
	               //nameValuePairs.add( new BasicNameValuePair("board[object_id]", ""));
	               //nameValuePairs.add( new BasicNameValuePair("utf8", "&#x2713;"));
	               //nameValuePairs.add( new BasicNameValuePair("authenticity_token", "nmO/idoZVv/cWObadFYKzZibwGZw520EfKMqTp0lVP0="));
	               
	               Iterator<NameValuePair> it = nameValuePairs.iterator();
	               while( it.hasNext()){
	            	   NameValuePair pair = it.next();
	            	   Log.i( CoinCoinApp.LOG_TAG, pair.getName() + " --> " + pair.getValue() ); 
	               }
	            	   
	               
	               //post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	               UrlEncodedFormEntity ent = new UrlEncodedFormEntity(nameValuePairs ,HTTP.UTF_8 );
	               post.setEntity( ent);
	              
	              
	               
	               Header[] headers = post.getAllHeaders();
	               
	               for( int i=0; i< headers.length; i++){
	            	   Log.i( CoinCoinApp.LOG_TAG, "POST-----------> " + ((Header)headers[i]).getName() + "=" + ((Header)headers[i]).getValue());
					
				}
	          
	               
	              
	               
	               HttpResponse response;
	               response=hClient.execute( post);
	               Log.i( CoinCoinApp.LOG_TAG, "RESPONSE-----------> " + response.getStatusLine());
	               Log.i( CoinCoinApp.LOG_TAG, "RESPONSE-----------> " + response.getStatusLine().getReasonPhrase());
	     
	               
	               
	               return true;
	          } catch (ClientProtocolException e) {
	               // TODO Auto-generated catch block
	               e.printStackTrace();
	               return false;
	          } catch (IOException e) {
	               // TODO Auto-generated catch block
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
