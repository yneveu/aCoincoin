package fr.gabuzomeu.aCoincoin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class CoinCoinDB extends SQLiteOpenHelper{

	private static String DB_PATH = "/data/data/fr.gabuzomeu.acoincoin/databases/";
	 
    private static String DB_NAME = "acoincoindb";
 
    private SQLiteDatabase db; 
 
    private final Context myContext;
	
	private static final String CREATE_TABLE_BOARDS="CREATE TABLE boards ( " +
			"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"name TEXT NOT NULL, " +
			"backend_url TEXT NOT NULL, " +
			"last_update INTEGER;)";
	
	private static final String CREATE_TABLE_MESSAGES="CREATE TABLE messages ( " +
			"fk_board_id INTEGER FOREIGN KEY" +
			"id INTEGER PRIMARY KEY AUTOINCREMENT" +
			"time INTEGER NOT NULL" +
			"info TEXT NOT NULL" +
			"message NOT NULL" +
			"login NOT NULL" +
			"FOREIGN KEY (fk_board_id) REFERENCES boards(id) ;)";
	
	
	public CoinCoinDB( Context context) {
		super(context, DB_NAME, null, 1);
        this.myContext = context;
	}


	
	
	public void createDataBase() throws IOException{
		 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		//do nothing - database already exist
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
 
    }
	
	
	private boolean checkDataBase(){
		 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e){
 
    		//database does't exist yet.
 
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
	
	private void copyDataBase() throws IOException{
		 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }
 
    @Override
	public synchronized void close() {
 
    	    if(db != null)
    		    db.close();
 
    	    super.close();
 
	}
 
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
}
