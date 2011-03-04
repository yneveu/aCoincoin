package fr.gabuzomeu.aCoincoin;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

 class CoincoinBoardsEditAdapterView extends LinearLayout {

	 CoinCoinApp app;
	 
	public CoincoinBoardsEditAdapterView(Context context, final CoincoinBoard board ) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setOrientation(HORIZONTAL);        
	    
   	    
		
		
	    //Name
            LinearLayout.LayoutParams messageParams = new LinearLayout.LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            
            messageParams.setMargins(1, 1, 1, 1);
            messageParams.weight=1;
            TextView messageControl = new TextView(context);
            messageControl.setHeight( 30);
            messageControl.setFocusable(true);
            messageControl.setClickable( true);
            messageControl.append(  board.getName() );
            this.setClickable(true);
            
            final Context mContext = context;
    
            OnClickListener ocl = new OnClickListener() {
				
				public void onClick(View v) {
					TextView tv = (TextView)v;
					Log.i(getClass().getSimpleName(), "ON Click: " + tv.getText());
					
					//Intent intent = new Intent( mContext , CoincoinBoardEditor.class);
					Intent intent = new Intent( mContext , CoincoinBoardSettings.class);
					intent.putExtra("board", board.getId());
					mContext.startActivity( intent);
				}
			};
            
            messageControl.setOnClickListener(ocl);
            addView( messageControl, messageParams);    
        }

}


public class CoincoinBoardsEditAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CoincoinBoard> boardsList;
    
    
    
    public CoincoinBoardsEditAdapter(Context context, ArrayList<CoincoinBoard> boardList ) { 
        this.context = context;
        this.boardsList = boardList;
    }

    public int getCount() {                        
        return boardsList.size();
    }

    public Object getItem(int position) {     
        return boardsList.get(position);
    }

    public long getItemId(int position) {  
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) { 
        CoincoinBoard board = boardsList.get(position);
        
       
        return new CoincoinBoardsEditAdapterView( this.context, board);
    }

}


