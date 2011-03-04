package fr.gabuzomeu.aCoincoin;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
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
	
	//MessageAdapter messageAdapter;
	
	SQLiteDatabase db = null;

	
	
	ListView lview;

	
	
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == 1) {
				Log.i( CoinCoinApp.LOG_TAG, "Service sent an update trigger");
				
				//app.setMessagesList( app.getNewMessages());
				//app.getMessageAdapter().
				//messageAdapter = new MessageAdapter( CoincoinActivity.this , app.getMessagesList());
				//app.setMessageAdapter( new MessageAdapter( mContext, android.R.layout.simple_list_item_1,app.getMessagesList()));
				app.getMessageAdapter().notifyDataSetChanged();
				onResume();
				//lview.setAdapter( app.getMessageAdapter());
				//lview.refreshDrawableState();
				
				
			}

		}
	};

	
	
		
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        app= (CoinCoinApp)this.getApplication();
        
        app.setActivityHandler( mHandler ); 
         
         activityContext = this;
         
         
         
       
        	Intent svc = new Intent(this, ACoinCoinService.class);
              
        	mContext.startService(svc);
        	
        	app.setMessagesList( app.getNewMessages());
        	lview = getListView();
        	app.getServiceHandler();
        	app.setMessageAdapter( new MessageAdapter( this, android.R.layout.simple_list_item_1,app.getMessagesList()));
        	//app.setMessageAdapter( messageAdapter);
        	setListAdapter( app.getMessageAdapter());
        	
        	getListView().setItemsCanFocus(true); 
        	
        	
        	this.getListView().setOnItemClickListener(new
            		AdapterView.OnItemClickListener() {
                public void onItemClick( AdapterView<?> parent, View v,
                                int position, long id) {
                		CoinCoinMessage mess = (CoinCoinMessage)app.getMessageAdapter().getItem( position);
                		Log.i( CoinCoinApp.LOG_TAG, "OnClick ------------> " + position);
                		Log.i( CoinCoinApp.LOG_TAG, "OnClick ------------> "  + mess.getMessage());
                }
    });
        	
        	
        	
          
    }
	
	
	
	@Override
	public void onResume() {
		Log.i( CoinCoinApp.LOG_TAG, "ON RESUME");
		super.onResume();
		app.resetNewMessages();
	
	//	MessageAdapter messageAdapter = new MessageAdapter(this, messagesList);
	//	setListAdapter(messageAdapter);
	}

	
	
	



	
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) 
	   {
	     super.onCreateOptionsMenu(menu);
	     
	     mItemPost = menu.add("Post");
	     mItemPost.setIcon(android.R.drawable.ic_menu_edit);
	     
	     mItemRefresh = menu.add("Refresh");
	     mItemRefresh.setIcon(android.R.drawable.ic_menu_rotate);
     
	     mItemPreferences = menu.add("Preferences");
	     mItemPreferences.setIcon( android.R.drawable.ic_menu_preferences);
	     
	     mItemAbout = menu.add("About");
	     mItemAbout.setIcon( android.R.drawable.ic_menu_help);
	     
	     mItemBoards = menu.add("Boards");
	     mItemBoards.setIcon( android.R.drawable.ic_menu_camera);
	     
	     return true;
	     
	     
	   }
	
	
	
	public boolean onOptionsItemSelected(MenuItem item)
	   {
	
	     if (item.hasSubMenu() == false){
	    	 
	    	 if( item ==  this.mItemRefresh ){
	    		 Message msg = new Message();
				 msg.arg1=MESSAGE_TO_SERVICE_REFRESH;
				 app.getServiceHandler().sendMessage(msg);
				 Log.i( CoinCoinApp.LOG_TAG, "MENU_REFRESH:" + MENU_REFRESH + " sent: " + item.getItemId()  );
	    	 }
	    	 if( item == this.mItemPost ){
	    		//CoincoinPalmipede palmi = new CoincoinPalmipede(mContext, app);
	    		 		//palmi.show();
	    		 
	    		 Log.i( CoinCoinApp.LOG_TAG, "MENU_POST" + MENU_POST + " sent: " + item.getItemId());
	    		 Intent intent = new Intent( mContext , CoincoinPostActivity.class);
				 //intent.putExtra("board", message.getBoardId());
				 //Log.i( CoinCoinApp.LOG_TAG, "SEND BOARD ID : " + message.getBoardId()  );
				 //intent.putExtra("norloge", hour + ":" + minutes +":" + seconds +" ");
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
	
	

}