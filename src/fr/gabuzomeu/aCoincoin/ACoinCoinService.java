package fr.gabuzomeu.aCoincoin;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;



public class ACoinCoinService extends Service {

	
	private Handler mHandler = new Handler();
	
	private Activity activity;
	private final long mDelay = 0;
	private final long defaultUpdateInterval = 240000;

	CoinCoinApp app;
	//SQLiteDatabase db;
	
	private boolean oneShotUpdate = false;

	
	private NotificationManager mNotificationManager;
	Notification notification;

	
	private Handler serviceHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == 1) {
				Log.i( CoinCoinApp.LOG_TAG, "Activity sent an update trigger");
				oneShotUpdate = true;
				mHandler.postDelayed( fetchTask, 100);
				
				
			}

		}
	};

	/** not using ipc... dont care about this method */
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		Log.i( CoinCoinApp.LOG_TAG, "created");
		app = (CoinCoinApp)this.getApplication();
		app.setServiceHandler( serviceHandler);
		_startService();
	}



	@Override
	public void onStart(final Intent intent, final int startId) {
		super.onStart(intent, startId);
		Log.i( CoinCoinApp.LOG_TAG, "started");
		//db = app.getDb();
		mHandler.postDelayed( fetchTask, 10000);
	}


	
	@Override
	public void onDestroy() {
		super.onDestroy();

		_shutdownService();


	}

	
	private void _startService() {

		Log.i( CoinCoinApp.LOG_TAG, "Timer started!!!");

	}




	private void _shutdownService() {
		
		Log.i( CoinCoinApp.LOG_TAG, "Timer stopped!!!");
	}

	private Runnable fetchTask = new Runnable() {
		
		
		
		
		
		
		public int fetchNewPosts(){
			
			Cursor cs = null;
			Cursor cs_mess=null;
			
			int newmessCounter=0;
			
			
			for( int i=0; i < app.getBoardList().size(); i++){
				CoincoinBoard board = app.getBoardList().get( i);
				Log.i( CoinCoinApp.LOG_TAG, "IN SERVICE BOARD -------> " + board.getName());

				try{
					URL url = new URL( board.getBackend_url() );
					
					SAXParserFactory spf = SAXParserFactory.newInstance(); 
					SAXParser sp = spf.newSAXParser(); 
					XMLReader xr = sp.getXMLReader(); 

					CoinCoinParser cParser = new CoinCoinParser(); 
					xr.setContentHandler( cParser); 
					xr.parse(new InputSource(url.openStream()));
					
					ArrayList<CoinCoinMessage> messageList = cParser.getMessages();
					Iterator<CoinCoinMessage> it = messageList.iterator();

					
					
					while( it.hasNext()){
						
						CoinCoinMessage mess = it.next();
						cs_mess = app.getDb().query( "messages", new String[]{"post_id"}, "post_id=? AND fk_board_id=?", new String[]{ String.valueOf(mess.getId()), String.valueOf( board.getId()) }, null, null, "time DESC");
						try{
							if( cs_mess.getCount() <= 0){
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
								board.setLastMessageId( mess.getId());
								cpt++;
								board.setNewMessages( cpt);
								Log.i( CoinCoinApp.LOG_TAG, "Avant ajout d'un message, adapter count:" + app.getMessageAdapter().getCount()  );
								app.getMessagesList().add( mess);
								app.getMessageAdapter().add( mess);
								Log.i( CoinCoinApp.LOG_TAG, "Après ajout d'un message, adapter count:" + app.getMessageAdapter().getCount()  );
							}else{
								//c
							}
							if( cs_mess != null){
								//Log.i( CoinCoinApp.LOG_TAG, "CLOSE CURSOR CS_MESS!!!!!!!!!!!!!!!!!!!!!!!!!!"  );
								cs_mess.close();
							}
						}catch(SQLException e){
							Log.i( CoinCoinApp.LOG_TAG, e.getMessage()  );
						}
					

					}
					
					if( newmessCounter > 0 ){  
						app.setUpdated( true);
					}
			
				}catch( Exception e){
					Log.i( CoinCoinApp.LOG_TAG, "APP !!!!!!!!!!!!!!!!!!!!!!!!!!" + e.toString()  );
					e.printStackTrace();
				}


			}
			

		
			

			if( cs != null){
				Log.i( CoinCoinApp.LOG_TAG, "CLOSE CURSOR CS!!!!!!!!!!!!!!!!!!!!!!!!!!"  );
				cs.close();
			}
			
			
			return newmessCounter;
			
		}
		
		
		
		
		
		
		
		
		
		
		
		public void run() {

			
			int icon = R.drawable.icon;
			CharSequence tickerText = "ACoincoin Messages";
			long when = System.currentTimeMillis();

			int newMessCounter = fetchNewPosts();
			
			notification = new Notification(icon, tickerText, when);
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			
			
			String notifMess = new String(); 
			
			if( newMessCounter > 0 ){  

								
				CharSequence contentTitle = "ACoincoin messages";
				
				for ( int i = 0; i < app.getBoardList().size(); i++ ){
					CoincoinBoard board = app.getBoardList().get( i);
					if( board.getNewMessages() > 0){
						//notifMess+= new String( board.getNewMessages()+ " messages added in board " + board.getName() + '\n');
						notifMess+= board.getName() + "(" + board.getNewMessages() + ") ";
						
					}
					
				}
				
				
				
				Intent notificationIntent = new Intent( ACoinCoinService.this, CoincoinActivity.class);

				PendingIntent contentIntent = PendingIntent.getActivity( ACoinCoinService.this , 0, notificationIntent, 0);
				
				notification.setLatestEventInfo( ACoinCoinService.this, contentTitle, notifMess, contentIntent);
				
				boolean sound = app.getPrefs().getBoolean( "Sounds" ,false);
				//boolean sound = Boolean.getBoolean( sSound);
				Log.i( CoinCoinApp.LOG_TAG, "Sound: " + sound);
				if( sound){
					notification.sound = Uri.parse("file:///sdcard/acoincoin/notification/coin.mp3");
				}
									
				
				mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				mNotificationManager.notify(1,notification);
				
				
				
				
				
			}
	
			//mHandler.postDelayed(this, defaultUpdateInterval);
			
			
			
			
			if( !oneShotUpdate){
				String sUp = app.getPrefs().getString( "Update interval","600");
				Log.i( CoinCoinApp.LOG_TAG, "PLOP " + sUp  );
				Long interval = Long.parseLong( sUp) * 1000;
				mHandler.postDelayed(this, interval );
				Log.i( CoinCoinApp.LOG_TAG, "Next Update in " + interval + " milliseconds");
			}
			
			//app.getLocMan().getLastKnownLocation( app.getLocMan().getBestProvider( null, true) );
			Message msg = new Message();
			msg.arg1=1;
			app.getActivityHandler().sendMessage(msg);
			
		}			
	};

}









