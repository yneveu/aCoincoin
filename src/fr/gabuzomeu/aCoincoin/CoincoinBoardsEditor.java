package fr.gabuzomeu.aCoincoin;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CoincoinBoardsEditor extends ListActivity {
	
	
	CoinCoinApp app;
	ArrayList<CoincoinBoard> boardsList;
	final Context mContext = this;
	MenuItem mItemAdd;

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        app= (CoinCoinApp)this.getApplication();
        boardsList = app.getBoardList();
        
        final CoincoinBoardsEditAdapter boardAdapter = new CoincoinBoardsEditAdapter( this, boardsList);
    	
    	setListAdapter( boardAdapter);
    	getListView().setItemsCanFocus(true);
    	getListView().setClickable(true);
    	
    	   	
    	
    	
    	this.getListView().setOnItemClickListener(new   AdapterView.OnItemClickListener() {
            public void onItemClick( AdapterView parent, View v, int position, long id) {
            		CoincoinBoard board = (CoincoinBoard)boardAdapter.getItem( position);
            		Log.i(getClass().getSimpleName(), "OnClick ------------> " + position);
            		Log.i(getClass().getSimpleName(), "OnClick ------------> "  + board.getName());
            }
    	});
	}
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) 
	   {
	     super.onCreateOptionsMenu(menu);
	     
	     mItemAdd = menu.add("Add");
	     mItemAdd.setIcon(android.R.drawable.ic_menu_edit);
	     

	     return true;
	     
	     
	   }
	
	public boolean onOptionsItemSelected(MenuItem item)
	   {
	
	     if (item.hasSubMenu() == false){
	    	 

	    	 if( item == this.mItemAdd ){
	    		 Intent intent = new Intent( mContext , BoardWizardActivity.class);
				 mContext.startActivity( intent);
	    	 }
	
        
	     }
	   return true;
	   }
	
}
