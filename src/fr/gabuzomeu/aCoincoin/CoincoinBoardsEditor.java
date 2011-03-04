package fr.gabuzomeu.aCoincoin;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CoincoinBoardsEditor extends ListActivity {
	
	
	CoinCoinApp app;
	ArrayList<CoincoinBoard> boardsList;
	
	
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
        
	
	
}
