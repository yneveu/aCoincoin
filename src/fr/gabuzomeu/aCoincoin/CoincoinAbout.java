package fr.gabuzomeu.aCoincoin;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;

public class CoincoinAbout extends Dialog {
	
	
	
	Context aContext;
	CoinCoinApp app;

	public CoincoinAbout(Context context, CoinCoinApp _app) {
		super(context);
		aContext = context;
		app=_app;
		
			
		setContentView(R.layout.about);
		setTitle("About");
		
		TextView aboutText = (TextView)findViewById( R.id.TextViewAbout);
		
		ArrayList<CoincoinBoard> boardList = app.getBoardList();
		
		for ( int i = 0; i < boardList.size(); i++){
			CoincoinBoard board = boardList.get(i);
			aboutText.append( "Board " + board.getName() + " : " + app.getNbMessages(board) + " messages\n");			
		}
		
		
	}

}
