package fr.gabuzomeu.aCoincoin;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

class BoardAdapterView extends LinearLayout {        
       
	
	
	public BoardAdapterView(Context context, final CoincoinBoard board ) {
            super( context );
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
            addView( messageControl, messageParams); 
            
            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            RadioButton button = new RadioButton( context);
            
            
            
            CompoundButton.OnCheckedChangeListener l=new CompoundButton.OnCheckedChangeListener(){
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					//Integer myPosition=(Integer)buttonView.getTag();
					Log.i( CoinCoinApp.LOG_TAG, "Checked: " + board.getName());
					//buttonView.setText(board.getName());
				}
			};

			button.setOnCheckedChangeListener(l);
			button.setText( board.getName() );
            
            addView( button, buttonParams);
        }

}


public class BoardAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CoincoinBoard> boardsList;
    
    RadioGroup rGroup = new RadioGroup( context); 
    
    public BoardAdapter(Context context, ArrayList<CoincoinBoard> boardList ) { 
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
        Log.i( CoinCoinApp.LOG_TAG, "AJOUT ELEMENT VIEW BOARDADAPTER: " + board);
        
        BoardAdapterView aView = new BoardAdapterView(  this.context, board );
        rGroup.addView( aView );
        return rGroup;
    }

	

}
