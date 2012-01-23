package fr.gabuzomeu.aCoincoin;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import fr.gabuzomeu.aCoincoin.CoincoinActivity.ResponseReceiver;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;



public class CoinCoinApp extends Application {
	
	SharedPreferences prefs;
	
	private static String DB_PATH = "/sdcard/acoincoin/databases";
	private static String DB_NAME = "acoincoindb";
	
	public static String LOG_TAG = "ACOINCOIN";
	
	private boolean updated=false;
	private Handler activityHandler;
	private Handler serviceHandler;
	ArrayList<CoinCoinMessage> messagesList;
	MessageAdapter messageAdapter;
	
	CoincoinActivity mainActivity;
	
	private ResponseReceiver receiver;
	
	private boolean onLaunch = true;
	
	

	public boolean isOnLaunch() {
		return onLaunch;
	}

	public void setOnLaunch(boolean onLaunch) {
		this.onLaunch = onLaunch;
	}

	private boolean boardsListUpdated = false;
		
	SQLiteDatabase db=null;
	
	public CoincoinActivity getMainActivity() {
		return mainActivity;
	}

	public void setMainActivity(CoincoinActivity mainActivity) {
		this.mainActivity = mainActivity;
	}
	
	
	public SharedPreferences getPrefs() {
		return prefs;
	}

	public void setPrefs(SharedPreferences prefs) {
		this.prefs = prefs;
	}

	private static final String CREATE_TABLE_BOARDS = "CREATE TABLE IF NOT EXISTS boards ( "
		+ "id INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ "name TEXT NOT NULL, "
		+ "backend_url TEXT NOT NULL, "
		+ "post_url TEXT,"
		+ "enabled BOOLEAN NOT NULL, "
		+ "background_color TEXT,"  
		+ "last_update LONG, "
		+ "login TEXT,"
		+ "cookie TEXT,"
		+ "host TEXT,"
		+ "post_referer TEXT,"
		+ "extra_post_params TEXT,"
		+ "user_agent TEXT,"
		+ "encoding TEXT );";

private static final String CREATE_TABLE_MESSAGES = "CREATE TABLE IF NOT EXISTS messages ( "
		+ "fk_board_id INTEGER , "
		+ "id INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ "time LONG NOT NULL, "
		+ "info TEXT, "
		+ "message TEXT NOT NULL, "
		+ "login TEXT, "
		+ "post_id INTEGER , "
		+ "FOREIGN KEY (fk_board_id) REFERENCES boards(id));";





	
	public LocationManager getLocMan() {
		return locMan;
	}

	public void setLocMan(LocationManager locMan) {
		this.locMan = locMan;
	}

	LocationManager locMan;
	
	
	
	public SQLiteDatabase getDb() {
		return db;
	}

	

	
	
	
	ArrayList<CoincoinBoard> boardList;
	
	
	
	public ArrayList<CoincoinBoard> getBoardList() {
		
		if (boardsListUpdated){
			getBoards();
			boardsListUpdated = false;
		}
		return boardList;
	}
	
	public CoincoinBoard getBoardById( int id){
		
		for( int i=0; i < boardList.size(); i++){
			CoincoinBoard tmpBoard= boardList.get( i);
			if( tmpBoard.getId() == id)
				return tmpBoard;
		}
		return null;
		
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setActivityHandler(Handler activityHandler) {
		this.activityHandler = activityHandler;
	}

	public Handler getActivityHandler() {
		return activityHandler;
	}

	public void setServiceHandler(Handler serviceHandler) {
		this.serviceHandler = serviceHandler;
	}

	public Handler getServiceHandler() {
		return serviceHandler;
	}
	
	@Override
	public void onCreate(){
		
		Log.i( CoinCoinApp.LOG_TAG, "APPLICATION START" );
		
		File fDir = new File( DB_PATH);
						
		if( !fDir.exists()){
			fDir.mkdirs();
		}
		
		messagesList = new ArrayList<CoinCoinMessage>();
		
		File fDB = new File( DB_PATH + File.separator + DB_NAME);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);		
		db=SQLiteDatabase.openOrCreateDatabase( fDB, null);
		
		getBoards();
		
		setMessagesList( messagesList);
		onLaunch = false;
	   setMessagesList( getNewMessages());
		   
        
   	Intent svc = new Intent(this, ICoincoinService.class);
		svc.addCategory( "onCreate");
    	this.startService(svc);
    	
    	
		
    	
    	
    	
	}
	
	private void getBoards(){
		
		boardList = new ArrayList<CoincoinBoard>();
		
		
		try{
			  db.execSQL( CREATE_TABLE_BOARDS  );
			  db.execSQL( CREATE_TABLE_MESSAGES );
		  }catch( SQLException e){
			  Log.i( CoinCoinApp.LOG_TAG, e.getMessage()  );
		  }
		
		
		Cursor cs = db.query( "boards", new String[]{"id","name","backend_url","last_update","post_url","enabled","background_color","login","cookie","host","post_referer","extra_post_params","user_agent","encoding"}, null, null , null, null, null);
		
		if( cs.getCount() > 0){
			do{
				cs.moveToNext();
				CoincoinBoard board = new CoincoinBoard();
				board.setId( cs.getInt(0));;
				board.setName(cs.getString(1));
				board.setBackend_url( cs.getString(2));
				board.setLast_update( cs.getLong(3) );
				board.setPostUrl( cs.getString(4));
				board.setEnabled( Boolean.parseBoolean( cs.getString( 5)));
				board.setBackground_color( cs.getString(6));
				board.setLogin( cs.getString(7));
				board.setCookies( cs.getString(8));
				board.setHost( cs.getString(9));
				board.setPostReferer( cs.getString(10));
				board.setExtraPostParams( cs.getString(11));
				board.setUserAgent( cs.getString(12));
				board.setEncoding( cs.getString(13));
				
				Cursor cs2 = db.query( "messages", new String[]{"post_id"},"fk_board_id=?", new String[] { String.valueOf(board.getId()) }  , null, null , "post_id", "1");
				if( cs2.getCount() > 0){
						cs2.moveToNext();
						board.setLastMessageId( cs2.getInt( 0));
					}
				else board.setLastMessageId( 0);
				
				Log.i( CoinCoinApp.LOG_TAG, "Board found -------> " + board + " LAST ID: " + board.getLastMessageId() );
				boardList.add( board);
			}while( !cs.isLast() );
		}else{
			Log.i( CoinCoinApp.LOG_TAG, "NO BOARD, CREATING SOME..."  );
			db.execSQL("insert into boards (name, backend_url, post_url, enabled, background_color, last_update, login, cookie, host, post_referer, encoding, extra_post_params, user_agent ) " +
						"VALUES ('gabuzomeu', 'http://gabuzomeu.fr/tribune.xml', 'http://gabuzomeu.fr/tribune/post', 'TRUE', '#dbe6e6',0, 'guest', 'vide','gabuzomeu.fr', 'http://gabuzomeu.fr/tribune/', 'utf8', '', 'aCoincoin 0.1' );");
			db.execSQL("insert into boards (name, backend_url, post_url, enabled, background_color, last_update, login, cookie, host, post_referer, encoding, extra_post_params, user_agent ) " +
						"VALUES ('linuxfr', 'http://linuxfr.org/board/remote.xml', 'http://linuxfr.org/board/', 'TRUE', '#aaffbb',0,'guest', 'vide', 'linuxfr.org', 'http://linuxfr.org/board/', 'utf8', 'section=1', 'aCoincoin 0.1' );");
			//db.execSQL("insert into boards (name, backend_url, post_url, enabled, background_color, last_update, login, cookie, host, post_referer, encoding, extra_post_params, user_agent ) " +
			//		"VALUES ('euromussels', 'http://euromussels.eu/tribune.xml', 'http://euromussels.eu/tribune/post', 'TRUE', '#84A5FF',0,'guest', 'vide', 'euromussels.eu', 'http://euromussels.eu/', 'utf8', '', 'aCoincoin 0.1' );");
			getBoards();
		}
		
	cs.close();
	}
	
	
	
	public ArrayList<CoinCoinMessage> getNewMessages(){
		
		int boardId;
		String boardName;
		String boardBackendUrl;
		long boardLastUpdate;
		String boardColor;
		
				
		try {
			Cursor cs = db.query("boards", new String[] { "id", "name",	"backend_url", "last_update", "background_color" }, null,null, null, null, null);
			do {
				
				cs.moveToNext();
				boardId = cs.getInt(0);
				boardName = cs.getString(1);
				boardBackendUrl = cs.getString(2);
				boardLastUpdate = cs.getLong(3);
				boardColor = cs.getString(4);

				
				try {
					Cursor cs2 = db.query(true, "messages", new String[] { "id",
							"time", "info", "message", "login", "post_id" },
							"fk_board_id=?", new String[] { String
									.valueOf(boardId) }, null, null, "time DESC",
							
							"30");
					
					Log.i( CoinCoinApp.LOG_TAG, "NB MESSAGES -------> "
							+ cs2.getCount() + " where fk_board_id=" + boardId
							+ " ***** ");

					do {
						// for each message found
						cs2.moveToNext();
						CoinCoinMessage mess = new CoinCoinMessage();

						mess.setTime(cs2.getLong(1));
						mess.setInfo(cs2.getString(2));
						mess.setMessage(cs2.getString(3));
						mess.setLogin(cs2.getString(4));
						mess.setId(cs2.getInt(5));
						mess.setBackgroundColor(boardColor);
						mess.setBoardId( boardId );

						messagesList.add(mess);
	
					} while (!cs2.isLast());
					cs2.close();


					Log.i( CoinCoinApp.LOG_TAG, messagesList.size()
							+ " Messages found");
				} catch (Exception e) {
					Log.i( CoinCoinApp.LOG_TAG, "EXECPTION -------> "
							+ e.getMessage());
				}

			} while (!cs.isLast());
			cs.close();
		} catch (Exception e) {
			Log.i( CoinCoinApp.LOG_TAG, "EXECPTION2 -------> "
					+ e.getMessage());
		}
		
		
		Collections.sort(messagesList);
		Collections.reverse(messagesList);
	
		return messagesList;
	}



	public void resetNewMessages(){
		for ( int i = 0; i < getBoardList().size(); i++ ){
			CoincoinBoard board = getBoardList().get( i);
			board.setNewMessages( 0);
		}
	}

	
	public int getNbMessages( CoincoinBoard board){
		int nb=0;
		try {
			Cursor cs = db.query("messages", new String[] { "id"}, "fk_board_id=?", new String[] {String.valueOf( board.getId()) }, null, null, null,null);
//			Log.i( CoinCoinApp.LOG_TAG, "NB MESSAGES -------> " + cs.getCount());
			nb = cs.getCount();
		}catch( Exception e){
			Log.i( CoinCoinApp.LOG_TAG, e.getMessage() );
		}
		
		return nb;
	}

	public void setBoardsListUpdated(boolean boardsListUpdated) {
		this.boardsListUpdated = boardsListUpdated;
	}

	public boolean isBoardsListUpdated() {
		return boardsListUpdated;
	}

	public ArrayList<CoinCoinMessage> getMessagesList() {
		return messagesList;
	}

	public void setMessagesList(ArrayList<CoinCoinMessage> messagesList) {
		this.messagesList = messagesList;
	}

	public MessageAdapter getMessageAdapter() {
		return messageAdapter;
	}

	public void setMessageAdapter(MessageAdapter messageAdapter) {
		this.messageAdapter = messageAdapter;
	}
	

	
	
	
	

}
