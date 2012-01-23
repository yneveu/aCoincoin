package fr.gabuzomeu.aCoincoin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.IntentService;
import android.app.Notification;
import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import fr.gabuzomeu.aCoincoin.CoincoinActivity.ResponseReceiver;



public class ICoincoinService extends IntentService {
	
	Notification notification;
	CoinCoinApp app;
	private boolean oneShotUpdate = false;
	
	
	
	private Handler serviceHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == 1) {
				Log.i( CoinCoinApp.LOG_TAG, "Activity sent an update trigger");
				oneShotUpdate = true;
				int newMessCounter = fetchNewPosts( false);
			}

		}
	};
	
	
	
	public ICoincoinService(){
		super( "ICoincoinService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		Log.i( CoinCoinApp.LOG_TAG, "ICoincoinService started");
		app = (CoinCoinApp)this.getApplication();
		app.setServiceHandler( serviceHandler);
		Message msg = serviceHandler.obtainMessage();
		msg.obj = intent;
        
		serviceHandler.sendMessage(msg);
		//mHandler.postDelayed( fetchTask, 10000);
		
		int newMessCounter = 0;
        
		if( intent.hasCategory( "onCreate") )
        	newMessCounter = fetchNewPosts( true );
        else
        	newMessCounter = fetchNewPosts( false );
	}
	
	
	
	@Override
	public void onStart(final Intent intent, final int startId) {
		super.onStart(intent, startId);
		
	}
	
			
		
	public int fetchNewPosts( boolean all){
			
			
			int newmessCounter=0;
			
			for( int i=0; i < app.getBoardList().size(); i++){
				CoincoinBoard board = app.getBoardList().get( i);
					
				try{
					URL url = new URL( board.getBackend_url() );
					File tmpFile = File.createTempFile( board.getName(), "coin");
					
					/***/
					
					BufferedInputStream in = new BufferedInputStream(url.openStream());
					FileOutputStream fos = new FileOutputStream( tmpFile.getPath());
					BufferedOutputStream bout = new BufferedOutputStream( fos,1024);
					byte[] data = new byte[1024];
					int x=0;
					while((x=in.read(data,0,1024))>=0){
						bout.write(data,0,x);
					}
					bout.close();
					in.close();
					
					/***/
					
					SAXParserFactory spf = SAXParserFactory.newInstance(); 
					SAXParser sp = spf.newSAXParser(); 
					XMLReader xr = sp.getXMLReader(); 

					CoinCoinParser cParser = new CoinCoinParser(); 
					xr.setContentHandler( cParser); 
					FileReader fr = new FileReader( tmpFile);
					xr.parse(new InputSource( fr));
					tmpFile.delete();
					ArrayList<CoinCoinMessage> messageList = cParser.getMessages();
					Iterator<CoinCoinMessage> it = messageList.iterator();

					
					int maxId = board.getLastMessageId();
					long maxTime=board.getLast_update();
					long maxTimeTmp = maxTime;
					int maxIdTmp = maxId;
					while( it.hasNext()){
						
						CoinCoinMessage mess = it.next();
						try{
							if( mess.getTime() > maxTime ){
								ContentValues messageValues= new ContentValues();
								messageValues.put("fk_board_id",  board.getId());
								messageValues.put("time",  mess.getTime());
								messageValues.put("info",  mess.getInfo());
								messageValues.put("login",  mess.getLogin());
								messageValues.put("message",  mess.getMessage());
								messageValues.put("post_id",  mess.getId());
								app.getDb().insert( "messages", "id", messageValues);
								newmessCounter++;
								int cpt = board.getNewMessages();
								
								if ( mess.getTime() > maxTimeTmp)
									maxTimeTmp = mess.getTime();
								
								cpt++;
								board.setNewMessages( cpt);
								mess.setBackgroundColor(  board.getBackground_color());
								app.getMessagesList().add( mess);
							}
						}catch(SQLException e){
							Log.i( CoinCoinApp.LOG_TAG, e.getMessage()  );
						}
					

					}
					
					if( newmessCounter > 0 ){  
						app.setUpdated( true);
						if( maxTimeTmp > -1){
							Log.i( CoinCoinApp.LOG_TAG, "NOUVEAU MAXID "  + maxTimeTmp );
							board.setLastMessageId( maxIdTmp);
							ContentValues boardValues= new ContentValues();
							boardValues.put( "last_update",maxTimeTmp );
							app.getDb().update( "boards", boardValues, "id=?", new String[]{String.valueOf(board.getId()) });
							board.setLast_update(maxTimeTmp );
							MessageAdapter ma = (MessageAdapter) app.getMainActivity().getListAdapter();
							Collections.sort( app.getMessagesList());
							Collections.reverse( app.getMessagesList());

							/*Send intent to activity for refresh*/
							Intent broadcastIntent = new Intent();
							broadcastIntent.setAction( ResponseReceiver.ACTION_RESP);
							broadcastIntent.addCategory( Intent.CATEGORY_DEFAULT);
							sendBroadcast(broadcastIntent);
						}
							
					}
					
			
				}catch( Exception e){
					Log.i( CoinCoinApp.LOG_TAG, "Oups: " + e.toString()  );
					e.printStackTrace();
				}

			}
		
			return newmessCounter;
			
		}
	


/*
	public void run() {

		
		int icon = R.drawable.icon;
		CharSequence tickerText = "ACoincoin Messages";
		long when = System.currentTimeMillis();

		int newMessCounter = fetchNewPosts( false);
		
		notification = new Notification(icon, tickerText, when);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		if( !oneShotUpdate){
			String sUp = app.getPrefs().getString( "Update interval","600");
			Log.i( CoinCoinApp.LOG_TAG, "PLOP " + sUp  );
			Long interval = Long.parseLong( sUp) * 1000;
		//	mHandler.postDelayed(this, interval );
			Log.i( CoinCoinApp.LOG_TAG, "Next Update in " + interval + " milliseconds");
		}
		oneShotUpdate = false;
		//app.getLocMan().getLastKnownLocation( app.getLocMan().getBestProvider( null, true) );
		
		if (app.isUpdated()){
			app.setUpdated( false);
			Message msg = new Message();
			msg.arg1=1;
			app.getActivityHandler().sendMessage(msg);
		}
		
	}			

*/


}
