package fr.gabuzomeu.aCoincoin;

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class BoardWizardActivity extends Activity {

	String chrisixBoardsList = "http://olcc.logicielslibres.info/boards_config.js";
	CoinCoinApp app;
	Context acontext;
	
	public void setApp(CoinCoinApp app) {
		this.app = app;
	}

	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		acontext = this;
		app = (CoinCoinApp)getApplication();

		setContentView(R.layout.board_wizard);
		
		Spinner spin = (Spinner)findViewById( R.id.boardsSpinner);
		ArrayAdapter<CoincoinBoard> adapter = null;
		adapter = new ArrayAdapter<CoincoinBoard>( this,android.R.layout.simple_spinner_item);
		
		try{
			File tmpFile = File.createTempFile( "chrisixBoards", "tmp");
			BufferedInputStream in = new BufferedInputStream( new URL(chrisixBoardsList).openStream());
			FileOutputStream fos = new FileOutputStream( tmpFile.getPath());
			BufferedOutputStream bout = new BufferedOutputStream( fos,1024);
			byte[] data = new byte[1024];
			int x=0;
			while((x=in.read(data,0,1024))>=0){
				bout.write(data,0,x);
			}
			bout.close();
			in.close();
		
			BufferedReader ibr = new BufferedReader( new FileReader( tmpFile));
			String strLine;
		
			
			
			ArrayList boardsdList = new ArrayList<CoincoinBoard>();
			
			
			
			while (( strLine = ibr.readLine()) != null)   {
				  // Print the content on the console
				 
				 Pattern p = Pattern.compile( "var\\s(\\S*).*=.*$");
				 Matcher m = p.matcher( strLine);

				 
				 while( m.find()){
					 Log.i( CoinCoinApp.LOG_TAG, " BOARD: " + m.group(1));
					 CoincoinBoard board= new CoincoinBoard();
					 board.setName( m.group(1));
					 
					 adapter.add( board);
					 boardsdList.add( m.group(1));
				 }
				 
			
				 
				 
			
			}
			ibr.close();
			ibr = new BufferedReader( new FileReader( tmpFile));
			
			
			
			for( int i = 0; i < boardsdList.size(); i++){
				boolean result = false;	
				Scanner scanner = new Scanner( tmpFile);
				while( scanner.hasNextLine() && !result){
					CoincoinBoard board = (CoincoinBoard)boardsdList.get( i);
					result = scanner.nextLine().indexOf( board.getName()) >= 0;
				}
				
			}
			
			 Log.i( CoinCoinApp.LOG_TAG, " APRES RESET: " + ibr.readLine());
		
		
		
		}catch( Exception e){
			e.printStackTrace();
		}
		
		
		
		
		Spinner boardsList = (Spinner)findViewById( R.id.boardsSpinner);
		spin.setAdapter( adapter);
	}
	
	
	
	
}
