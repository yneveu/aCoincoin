package fr.gabuzomeu.aCoincoin;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class CoincoinActivity extends ListActivity {

	final Context mContext = this;
	Context activityContext;

	CoinCoinApp app;

	
	private int MENU_REFRESH;
	private int MENU_POST;
	int MESSAGE_TO_SERVICE_REFRESH=1;
	
	MenuItem mItemPost;
	MenuItem mItemRefresh;
	MenuItem mItemPreferences;
	MenuItem mItemAbout;
	MenuItem mItemBoards;
	
	SQLiteDatabase db = null;
	private ResponseReceiver receiver;
	ListView lview;
	boolean onLaunch = true; 

	
	/*private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == 1) {
				Log.i( CoinCoinApp.LOG_TAG, "CoincoinActivity - Service sent an update trigger");
				
				//app.setMessagesList( app.getNewMessages());
				//app.getMessageAdapter().
				//messageAdapter = new MessageAdapter( CoincoinActivity.this , app.getMessagesList());
				//app.setMessageAdapter( new MessageAdapter( mContext, android.R.layout.simple_list_item_1,app.getMessagesList()));
				//app.getMessageAdapter().notifyDataSetChanged();
				
				 
				CoinCoinMessage mess = app.getMessagesList().get( app.getMessagesList().size()-1);
				Log.i( CoinCoinApp.LOG_TAG, "DANS MESSAGELIST, LAST= " + mess.getMessage());
				MessageAdapter adapt = (MessageAdapter)getListAdapter();
				
				
				adapt.notifyDataSetChanged();
				
				
				//lview.setAdapter( app.getMessageAdapter());
				//lview.refreshDrawableState();
				
				
			}

		}
	};
*/
	
	
		
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        /*To catch intents from ICoincoinService*/
        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        
  		/*Called on application start only*/
        if( onLaunch){
        	onLaunch = false;
        	app= (CoinCoinApp)this.getApplication();
     //       app.setActivityHandler( mHandler ); 
            activityContext = this;
            app.setMainActivity( this);
            Log.i( CoinCoinApp.LOG_TAG, "ON CREATE ACTIVITY");
            MessageAdapter adapt = new MessageAdapter( this, R.layout.messagerow, app.getMessagesList());
            app.setMessageAdapter( adapt);
            setListAdapter( adapt);
            lview = getListView();
            lview.setFastScrollEnabled( true);
            app.getMessageAdapter().notifyDataSetChanged();
        	
            /*Refresh at launch*/
        	Intent svc = new Intent(this, ICoincoinService.class);
        	svc.addCategory( "onCreate");
        	this.startService(svc);
        }
    }
	
	
	protected void onPause() {

		if( receiver != null){
			unregisterReceiver( receiver);
			Log.i( CoinCoinApp.LOG_TAG, "On pause Receiver unregistered");
			receiver = null;
		}
		super.onPause();
		
	}
	
	protected void onStop() {

		if( receiver != null){
			unregisterReceiver( receiver);
			Log.i( CoinCoinApp.LOG_TAG, "On stop Receiver unregistered");
			receiver = null;
		}
		super.onStop();
		
	}
	
	protected void onDestroy() {

		if( receiver != null){
			unregisterReceiver( receiver);
			Log.i( CoinCoinApp.LOG_TAG, "On destroy Receiver unregistered");
			receiver = null;
		}
		super.onDestroy();
		
	}
	
	
	@Override
	public void onResume() {
		Log.i( CoinCoinApp.LOG_TAG, "ON RESUME ACTIVITY");
		receiver = new ResponseReceiver();
		IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
       	registerReceiver(receiver, filter);
       	
		//app.resetNewMessages();
        super.onResume();
		
	}


	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) 
	   {
	     super.onCreateOptionsMenu(menu);
	     
	     mItemPost = menu.add("Post");
	     mItemPost.setIcon(android.R.drawable.ic_menu_edit);
	     
	     mItemRefresh = menu.add("Refresh");
	     mItemRefresh.setIcon(android.R.drawable.ic_menu_rotate);
     
/*	     mItemPreferences = menu.add("Preferences");
	     mItemPreferences.setIcon( android.R.drawable.ic_menu_preferences);
	     
	     mItemAbout = menu.add("About");
	     mItemAbout.setIcon( android.R.drawable.ic_menu_help);
	     
	     mItemBoards = menu.add("Boards");
	     mItemBoards.setIcon( android.R.drawable.ic_menu_camera);
	*/    
	     return true;
	     
	     
	   }
	
	
	
	public boolean onOptionsItemSelected(MenuItem item)
	   {
	
	     if (item.hasSubMenu() == false){
	    	 
	    	 if( item ==  this.mItemRefresh ){
					Intent svc = new Intent(this, ICoincoinService.class);
			    	this.startService(svc);
				 
				 Log.i( CoinCoinApp.LOG_TAG, "MENU_REFRESH:" + MENU_REFRESH + " sent: " + item.getItemId()  );
	    	 }
	    	 if( item == this.mItemPost ){
	    		//CoincoinPalmipede palmi = new CoincoinPalmipede(mContext, app);
	    		 		//palmi.show();
	    		 
	    		 Log.i( CoinCoinApp.LOG_TAG, "MENU_POST" + MENU_POST + " sent: " + item.getItemId());
	    		 Intent intent = new Intent( mContext , CoincoinPostActivity.class);
				 mContext.startActivity( intent);

	    	 }
	    	 if( item == this.mItemPreferences){
	    		 Log.i( CoinCoinApp.LOG_TAG, "Go to preferences...");
	    		 startActivity(new Intent(this, CoincoinPrefsActivity.class));
	    	 }
	    	 
	    	 if( item == this.mItemBoards){
	    		 Log.i( CoinCoinApp.LOG_TAG, "Go to boards...");
	    		 startActivity(new Intent(this, CoincoinBoardsEditor.class));
	    	 }
	    	 
	    	 if( item == this.mItemAbout){
	    		 Log.i( CoinCoinApp.LOG_TAG, "Go to about...");
	    		 CoincoinAbout about = new CoincoinAbout(mContext, app);
	    		 about.show();
	    	 }
	          
	     return true;
	     }
	     return true;
	   }
	
	
	/*Called when ICoincoin service has finished*/
	public class ResponseReceiver extends BroadcastReceiver {
		   public static final String ACTION_RESP = "fr.gabuzomeu.acoincoin.action.REFRESH_BOARDS";
		   
		   @Override
		    public void onReceive(Context context, Intent intent) {
			   Log.i( CoinCoinApp.LOG_TAG, "CoincoinActivity - Intent received - Refresh boards");
			   app.getMessageAdapter().notifyDataSetChanged();
		    }
		}
	
	
	
	

}