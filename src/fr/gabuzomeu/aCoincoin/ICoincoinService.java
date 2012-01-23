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

import fr.gabuzomeu.aCoincoin.CoincoinActivity.ResponseReceiver;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;



public class ICoincoinService extends IntentService {
	
	private Handler mHandler = new Handler();
	private NotificationManager mNotificationManager;
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
			//	mHandler.postDelayed( fetchTask, 100);
				//oneShotUpdate = false;
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
			
			//Cursor cs = null;
			//Cursor cs_mess=null;
			
			int newmessCounter=0;
			
			
			for( int i=0; i < app.getBoardList().size(); i++){
				CoincoinBoard board = app.getBoardList().get( i);
				Log.i( CoinCoinApp.LOG_TAG, "IN ISERVICE BOARD -------> " + board.getName() + "MAX ID: " + board.getLastMessageId());

				
				
				
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
						//cs_mess = app.getDb().query( "messages", new String[]{"post_id"}, "post_id=? AND fk_board_id=?", new String[]{ String.valueOf(mess.getId()), String.valueOf( board.getId()) }, null, null, "time DESC");
						
						//Log.i( CoinCoinApp.LOG_TAG, "ID du message:" + mess.getId() + " ID max board: " + board.getLastMessageId()  );
						
						try{
							if( mess.getTime() > maxTime ){
								Log.i( CoinCoinApp.LOG_TAG, "AJOUT message:" + mess.getTime() + " ID max board: " + maxTime + " MAXID TMP: " +maxTimeTmp );
								//if( cs_mess.getCount() <= 0){
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
								//app.getMessageAdapter().add( mess);
								MessageAdapter ma = (MessageAdapter)app.getMainActivity().getListAdapter();
								
								//ma.add( mess);
								
							}else{
							//	Log.i( CoinCoinApp.LOG_TAG, "PAS D'AJOUT DU MESSAGE" + mess.getId() + " ID max board: " + maxId + " MAXID TMP: " +maxIdTmp );
							}
							//if( cs_mess != null){
								//Log.i( CoinCoinApp.LOG_TAG, "CLOSE CURSOR CS_MESS!!!!!!!!!!!!!!!!!!!!!!!!!!"  );
						//		cs_mess.close();
						//	}
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
							int nbMessages = app.getMessagesList().size();
							board.setLast_update(maxTimeTmp );
							//app.getMessageAdapter().notifyDataSetChanged();
							MessageAdapter ma = (MessageAdapter) app.getMainActivity().getListAdapter();
							Log.i( CoinCoinApp.LOG_TAG, "ADAPTEUR: " + ma + " GETCOUNT AVANT "  + ma.getCount());
							Collections.sort( app.getMessagesList());
							Collections.reverse( app.getMessagesList());
							//app.getMessageAdapter().notifyDataSetChanged();
							//ma.notifyDataSetChanged();
							Log.i( CoinCoinApp.LOG_TAG, "GETCOUNT APRES "  + ma.getCount());
							
							/*Send intent to activity for refresh*/
							Intent broadcastIntent = new Intent();
							broadcastIntent.setAction( ResponseReceiver.ACTION_RESP);
							broadcastIntent.addCategory( Intent.CATEGORY_DEFAULT);
							//broadcastIntent.putExtra(P ARAM_OUT_MSG, resultTxt);
							sendBroadcast(broadcastIntent);
						//	for (int i1=0; i1< nbMessages; i1++ ){
						//		Log.i( CoinCoinApp.LOG_TAG, "LISTE " + i1 + " : " + app.getMessagesList().get( i1));
					     //   }
						}
							
					}
					
					
			
				}catch( Exception e){
					Log.i( CoinCoinApp.LOG_TAG, "APP !!!!!!!!!!!!!!!!!!!!!!!!!!" + e.toString()  );
					e.printStackTrace();
				}

			}
		
			return newmessCounter;
			
		}
	








	public void run() {

		
		int icon = R.drawable.icon;
		CharSequence tickerText = "ACoincoin Messages";
		long when = System.currentTimeMillis();

		int newMessCounter = fetchNewPosts( false);
		
		notification = new Notification(icon, tickerText, when);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		
		String notifMess = new String(); 
		
	/*	if( newMessCounter > 0 ){  

							
			CharSequence contentTitle = "ACoincoin messages";
			
			for ( int i = 0; i < app.getBoardList().size(); i++ ){
				CoincoinBoard board = app.getBoardList().get( i);
				if( board.getNewMessages() > 0){
					//notifMess+= new String( board.getNewMessages()+ " messages added in board " + board.getName() + '\n');
					notifMess+= board.getName() + "(" + board.getNewMessages() + ") ";
					
				}
				
			}
			Intent notificationIntent = new Intent( ICoincoinService.this, CoincoinActivity.class);

			notificationIntent.setAction("android.intent.action.MAIN");
			notificationIntent.addCategory("android.intent.category.LAUNCHER");
			//PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
			
			PendingIntent contentIntent = PendingIntent.getActivity( ICoincoinService.this , 0, notificationIntent, 0);
			
			notification.setLatestEventInfo( ICoincoinService.this, contentTitle, notifMess, contentIntent);
			
			boolean sound = app.getPrefs().getBoolean( "Sounds" ,false);
			//boolean sound = Boolean.getBoolean( sSound);
			Log.i( CoinCoinApp.LOG_TAG, "Sound: " + sound);
			if( sound){
				notification.sound = Uri.parse("file:///sdcard/acoincoin/notification/coin.mp3");
			}
						

			MessageAdapter messageAdapter = (MessageAdapter) app.getMainActivity().getListAdapter();
			//messageAdapter.notifyDataSetInvalidated();
			
			messageAdapter.notifyDataSetChanged();
			
			Log.i( CoinCoinApp.LOG_TAG, "PLLLLLLLLLLLLLLLLLOOOOOOOOOOOOOOPPPPPPPPPPPPPP !!!!!!!!!!!!!!!!!!!!!!!!!!" );
			//app.getMainActivity().getListView().invalidate();
			//app.getMainActivity().getListView().refreshDrawableState();
			mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			mNotificationManager.notify(1,notification);
			
			
			
			
			
		}*/

		//mHandler.postDelayed(this, defaultUpdateInterval);
		
		
		
		
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


















}
